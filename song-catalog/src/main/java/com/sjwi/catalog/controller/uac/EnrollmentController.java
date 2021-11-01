package com.sjwi.catalog.controller.uac;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.exception.InvalidOperationException;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.security.EnrollmentToken;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.TokenService;
import com.sjwi.catalog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

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
	public ModelAndView enroll(@RequestParam (name="user", required = false) String user,
			@RequestParam(name="token", required = false) String token,
			Authentication auth,
			HttpServletRequest request, HttpServletResponse response) {
		ModelAndView mv = new ModelAndView("forward:/");
		try {
			EnrollmentToken enrollmentToken = tokenService.getEnrollmentToken(user).setSessionToken(token);
			if (enrollmentToken.isTokenValid()) {
				mv.addObject("token", token);
				mv.addObject("user", user);
				return mv;
			} else
				throw new InvalidOperationException();
		} catch (NullPointerException | InvalidOperationException e ) {
			mv.addObject("ALERT_ON_LOAD", "BAD_ENROLLMENT_TOKEN");
			return mv;
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = {"/enroll"}, method = RequestMethod.POST)
	public ModelAndView enroll(HttpServletRequest request, HttpServletResponse response,
			@RequestParam (name = "username", required = true) String username,
			@RequestParam (name = "password", required = true) String password,
			@RequestParam (name = "email", required = true) String email,
			@RequestParam (name = "firstName", required = true) String firstName,
			@RequestParam (name = "lastName", required = true) String lastName,
			@RequestParam (name = "token", required = true) String requestToken,
			@RequestParam (name = "user", required = true) String requestUser,
			Authentication auth
			) {
		try {
			username = username.toLowerCase();
			EnrollmentToken token = tokenService.getEnrollmentToken(email).setSessionToken(requestToken);
			if (token.isTokenValid()) {
				List<String> authorities = new ArrayList<String>(Arrays.asList("USER"));
				if (("ADMIN").equals(token.getRole()))
					authorities.add("ADMIN");
				CfUser user = userService.createUser(username,firstName,lastName,email,password, authorities, null);
				request.login(user.getUsername(), password);
				tokenService.deleteEnrollmentToken(token.getId());
				logger.logMessageWithEmail("New user enrolled: " + username);
				logger.logSignIn(request, username);
				return new ModelAndView("redirect:/home");
			} else
				throw new ServletException("Invalid token");
		} catch (ServletException e) {
			ModelAndView mv = new ModelAndView("forward:/");
			mv.addObject("ALERT_ON_LOAD", "BAD_ENROLLMENT_TOKEN");
			controllerHelper.errorHandler(e);
			return mv;
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
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
