package com.sjwi.catalog.aspect;

import java.io.IOException;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.UserService;

@Aspect
@Configuration
public class LandingPageSessionInitializer {

	@Autowired
	CustomLogger log;
	
	@Autowired
	ControllerHelper controllerHelper;

	@Autowired
	@Lazy
	UserService userService;

	@Before("@annotation(com.sjwi.catalog.aspect.LandingPageAspect)")
	public void logPageRequest(JoinPoint joinPoint) throws IOException {
		controllerHelper.setCookiesInSession();
		controllerHelper.attemptUserLoginViaCookie();
		CfUser demoUser = (CfUser) userService.loadUserByUsername("demo");
		SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(demoUser, null, demoUser.getAuthorities()));
		controllerHelper.setSessionPreferences(demoUser);
	}
}
