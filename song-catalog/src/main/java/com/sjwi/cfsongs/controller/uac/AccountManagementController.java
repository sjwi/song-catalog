package com.sjwi.cfsongs.controller.uac;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;

import javax.security.auth.login.LoginException;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.exception.InvalidOperationException;
import com.sjwi.cfsongs.exception.MailException;
import com.sjwi.cfsongs.exception.PasswordException;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.mail.MailConstants;
import com.sjwi.cfsongs.mail.Mailer;
import com.sjwi.cfsongs.model.ResponseMessage;
import com.sjwi.cfsongs.model.mail.EmailFromTemplate;
import com.sjwi.cfsongs.model.security.EnrollmentToken;
import com.sjwi.cfsongs.model.security.PasswordResetToken;
import com.sjwi.cfsongs.model.security.SecurityToken;
import com.sjwi.cfsongs.model.user.CfUser;
import com.sjwi.cfsongs.service.TokenService;
import com.sjwi.cfsongs.service.UserService;

@Controller
public class AccountManagementController {

	private final static String INVITE_MSG_TEMPLATE = "email-templates/invitation.html";
	private final static String RESET_PASSWORD_MSG_TEMPLATE = "email-templates/password-reset.html";
	private final static String LOGO_IMG_PATH = "static/images/logo_transparent.png";
	private final static String LOGO_IMG_PATH_DARK = "static/images/logo_transparent_dark.png";

	@Autowired
	ControllerHelper controllerHelper;

	@Autowired
	CustomLogger logger;

	@Autowired
	ServletContext context;
	
	@Autowired
	UserService userService;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	Mailer mailer;
	
	@RequestMapping(value = {"/change-password"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage changePassword(HttpServletRequest request, HttpServletResponse response,
			@RequestParam (name = "password", required = true) String oldPassword,
			@RequestParam (name = "newPassword", required = true) String newPassword,
			@RequestParam (name = "confirmPassword", required = true) String confirmPassword,
			Authentication auth,
			Principal principal) throws IOException {
		try {
			userService.changePassword(principal.getName(), oldPassword, newPassword);
			return new ResponseMessage("success");
		} catch (PasswordException e) {
			return new ResponseMessage("bad_password");
		}
	}

	@RequestMapping("/send-invite")
	@ResponseBody
	public ResponseMessage sendInvite(@RequestParam (name = "invitee",required=true) String invitee, @RequestParam (name = "role",required=true) String role,Authentication auth,HttpServletRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException, ServletException, MailException {
		if(userService.getAllActiveUsers().stream().anyMatch(u -> u.getEmail().equalsIgnoreCase(invitee))) {
			return new ResponseMessage("user_already_exists");
		}
		if (userService.getAllEnrollmentEmails().stream().anyMatch(u -> u.equalsIgnoreCase(invitee))){
			return new ResponseMessage("user_already_invited");
		}
		EnrollmentToken token = new EnrollmentToken(invitee.toLowerCase(),role);
		tokenService.storeEnrollmentToken(token);
		try {
			mailer.sendMail(new EmailFromTemplate()
					.setMsgArgs(new String[] {controllerHelper.buildTokenLink(request, token, "enroll")})
					.setImagePath(new ArrayList<String>(Arrays.asList(LOGO_IMG_PATH, LOGO_IMG_PATH_DARK)))
					.setMsgTemplate(INVITE_MSG_TEMPLATE)
					.setTo(invitee)
					.setSubject(MailConstants.INVITATION_SUBJECT.replace("{{USERNAME}}", ((CfUser)auth.getPrincipal()).getFullName())));
			logger.logMessageWithEmail("Invitation sent to " + invitee + " from " + auth.getName());
		} catch (MailException e) {
			controllerHelper.errorHandler(e);
			return new ResponseMessage("bad_email");
		}
		return new ResponseMessage("success");
	}
	@RequestMapping(value = "/password-reset", method= RequestMethod.POST)
	@ResponseBody
	public ResponseMessage sendResetPasswordEmail(@RequestParam (name = "email",required=true) String email,HttpServletRequest request) throws InvalidKeySpecException, NoSuchAlgorithmException, ServletException, MailException {
		try {
			CfUser user = (CfUser) userService.loadUserByUsername(email);
			if (user == null)
				throw new InvalidOperationException("No username associated with this email");
			SecurityToken token = new PasswordResetToken(user.getUsername());
			tokenService.storePasswordResetToken(token);
			mailer.sendMail(new EmailFromTemplate()
					.setMsgArgs(new String[] {user.getUsername(),controllerHelper.buildTokenLink(request, token, "reset-password")})
					.setImagePath(new ArrayList<String>(Arrays.asList(LOGO_IMG_PATH, LOGO_IMG_PATH_DARK)))
					.setMsgTemplate(RESET_PASSWORD_MSG_TEMPLATE)
					.setTo(user.getEmail())
					.setSubject(MailConstants.PASSWORD_RESET_SUBJECT));
			logger.logMessageWithEmail("Password reset link sent to " + user.getUsername());
		} catch (MailException | InvalidOperationException e) {
			controllerHelper.errorHandler(e);
			return new ResponseMessage("bad_email");
		}
		return new ResponseMessage("success");
	}

	@RequestMapping(value = "/reset-password", method = RequestMethod.GET)
	public String promptForPasswordReset(@RequestParam (name="user", required = true) String user,
			@RequestParam(name="token", required = true) String token,
			Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws  IOException {
		try {
			CfUser cfUser = (CfUser) userService.loadUserByUsername(user);
			SecurityToken securityToken = tokenService.getPasswordResetToken(cfUser.getUsername()).setSessionToken(token);
			if (securityToken.isTokenValid()) {
				request.setAttribute("token", token);
				request.setAttribute("user", user);
				return "forward:/";
			} else {
				throw new InvalidOperationException("Invalid Secuirty Token");
			}
		} catch (NullPointerException | InvalidOperationException e ) {
			request.setAttribute("ALERT_ON_LOAD", "BAD_RESET_TOKEN");
			return "forward:/";
		}
	}
	
	@RequestMapping(value = "/reset-password", method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage resetPassword(@RequestParam (name="user", required = true) String user,
			@RequestParam(name="token", required = true) String token,
			@RequestParam(name="newPassword",required = true) String password,
			Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws  IOException, PasswordException, LoginException, ServletException {
		CfUser cfUser = (CfUser) userService.loadUserByUsername(user);
		SecurityToken securityToken = tokenService.getPasswordResetToken(cfUser.getUsername()).setSessionToken(token);
		if (securityToken.isTokenValid()) {
			userService.resetPassword(cfUser.getUsername(), password);
			request.login(cfUser.getUsername(), password);
			logger.logSignIn(request, cfUser.getUsername());
			controllerHelper.setSessionPreferences(cfUser, request, response);
			tokenService.deletePasswordResetToken(cfUser.getUsername());
			return new ResponseMessage("success");
		} else {
			throw new LoginException();
		}
	}
}
