package com.sjwi.catalog.aspect;

import java.io.IOException;

import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class LandingPageSessionInitializer {

	@Autowired
	CustomLogger log;
	
	@Autowired
	ControllerHelper controllerHelper;

	@Before("@annotation(com.sjwi.catalog.aspect.LandingPageAspect)")
	public void logPageRequest(JoinPoint joinPoint) throws IOException {
		controllerHelper.setCookiesInSession();
		controllerHelper.attemptUserLoginViaCookie();
		if (!ServletConstants.IS_INITIALIZED)
			ServletConstants.initializeServletConstants();
	}
}
