package com.sjwi.cfsongs.controller.uac;

import static com.sjwi.cfsongs.model.security.StoredCookieToken.STORED_COOKIE_TOKEN_KEY;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.ResponseMessage;
import com.sjwi.cfsongs.model.security.SecurityToken;
import com.sjwi.cfsongs.model.security.StoredCookieToken;
import com.sjwi.cfsongs.model.user.CfUser;
import com.sjwi.cfsongs.service.TokenService;
import com.sjwi.cfsongs.service.UserService;

@Controller
public class LoginController {

	@Autowired
	CustomLogger logger;

	@Autowired
	UserService userService;
	
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	TokenService tokenService;
	
	@RequestMapping(value = {"/login"}, method = RequestMethod.GET)
	public String login(HttpServletRequest request, Authentication auth, HttpServletResponse response) throws IOException {
		request.setAttribute("LOGIN", true);
		return "forward:/home";
	}

	@RequestMapping(value = {"/login"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage login(HttpServletRequest request, HttpServletResponse response,
			@RequestParam (name = "username", required = true) String username,
			Principal principal, Authentication auth,
			@RequestParam (name = "password", required = true) String password) throws IOException {
		try {
			request.login(username, password);
			logger.logSignIn(request, username);
			controllerHelper.setSessionPreferences((CfUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal(), request, response);
			generateCookieToken(request, response, username);
			return new ResponseMessage("success");
		} catch (ServletException e) {
			e.printStackTrace();
			return new ResponseMessage("bad_password");
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseMessage("bad_login_attempt");
		}
	}

	private void generateCookieToken(HttpServletRequest request, HttpServletResponse response, String user) {
		try {
			SecurityToken cookieToken = new StoredCookieToken(user);
			tokenService.storeCookieToken(cookieToken);
			response.addCookie(controllerHelper.buildStaticCookie(request.getServerName(), STORED_COOKIE_TOKEN_KEY, cookieToken.getTokenString(),request.getCookies()));
		} catch (InvalidKeySpecException | NoSuchAlgorithmException e) {
			logger.logErrorWithEmail(e.getMessage());
		}
	}
}
