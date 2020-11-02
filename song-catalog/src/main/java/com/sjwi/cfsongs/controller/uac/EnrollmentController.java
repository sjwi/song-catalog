package com.sjwi.cfsongs.controller.uac;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.security.EnrollmentToken;
import com.sjwi.cfsongs.model.user.CfUser;
import com.sjwi.cfsongs.service.TokenService;
import com.sjwi.cfsongs.service.UserService;

@Controller
public class EnrollmentController {

	@Autowired
	ControllerHelper controllerHelper;

	@Autowired
	UserService userService;
	
	@Autowired
	TokenService tokenService;

	@Autowired
	CustomLogger logger;

	@RequestMapping(value = {"/enroll"}, method = RequestMethod.GET)
	public String enroll(@RequestParam (name="user", required = false) String user,
			@RequestParam(name="token", required = false) String token,
			Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		try {
			EnrollmentToken enrollmentToken = tokenService.getEnrollmentToken(user).setSessionToken(token);
			if (enrollmentToken.isTokenValid()) {
				request.setAttribute("token", token);
				request.setAttribute("user", user);
				return "forward:/";
			} else {
				throw new InvalidOperationException();
			}
		} catch (NullPointerException | InvalidOperationException e ) {
			request.setAttribute("ALERT_ON_LOAD", "BAD_ENROLLMENT_TOKEN");
			return "forward:/";
		}
	}

	@RequestMapping(value = {"/enroll"}, method = RequestMethod.POST)
	public String enroll(HttpServletRequest request, HttpServletResponse response,
			@RequestParam (name = "username", required = true) String username,
			@RequestParam (name = "password", required = true) String password,
			@RequestParam (name = "email", required = true) String email,
			@RequestParam (name = "firstName", required = true) String firstName,
			@RequestParam (name = "lastName", required = true) String lastName,
			@RequestParam (name = "token", required = true) String requestToken,
			@RequestParam (name = "user", required = true) String requestUser,
			Authentication auth
			) throws IOException {
		try {
			EnrollmentToken token = tokenService.getEnrollmentToken(email).setSessionToken(requestToken);
			if (token.isTokenValid()) {
				List<String> authorities = new ArrayList<String>(Arrays.asList("USER"));
				if (("ADMIN").equals(token.getRole())){
					authorities.add("ADMIN");
				}
				CfUser user = userService.createUser(username,firstName,lastName,email,password, authorities, null);
				request.login(user.getUsername(), password);
				tokenService.deleteEnrollmentToken(token.getId());
				logger.logMessageWithEmail("New user enrolled: " + username);
				logger.logSignIn(request, username);
				return "redirect:/home";
			} else {
				throw new ServletException("Invalid token");
			}
		} catch (ServletException e) {
			request.setAttribute("ALERT_ON_LOAD", "BAD_ENROLLMENT_TOKEN");
			controllerHelper.errorHandler(e);
			return "forward:/";
		}
	}

	@RequestMapping("/distinct-username")
	@ResponseBody
	public String checkUsername(@RequestParam (name="username",required=true) String username) {
		if (userService.isUsernameTaken(username)) {
			return "{\"status\" : \"taken_username\"}";
		} else if (username.contains(" ")) {
			return "{\"status\" : \"bad_username\"}";
		} else {
			return "{\"status\" : \"free_username\"}";
		}
	}
}
