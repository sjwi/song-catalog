package com.sjwi.cfsongs.aspect;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.log.CustomLogger;

@Aspect
@Configuration
public class LandingPageSessionInitializer {

	@Autowired
	CustomLogger log;
	
	@Autowired
	ControllerHelper controllerHelper;

	@Before("@annotation(com.sjwi.cfsongs.aspect.LandingPageAspect)")
	public void logPageRequest(JoinPoint joinPoint) throws IOException {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
		controllerHelper.setCookiesInSession(request);
		controllerHelper.attemptUserLoginViaCookie(request, response);
	}
}
