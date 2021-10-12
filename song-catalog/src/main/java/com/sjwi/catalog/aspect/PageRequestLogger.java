package com.sjwi.catalog.aspect;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.service.UserService;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

@Aspect
@Configuration
public class PageRequestLogger {

	@Autowired
	CustomLogger log;
	
	@Autowired
	ControllerHelper controllerHelper;

	@Autowired
	UserService userService;

	@Before("(execution(* com.sjwi.catalog.controller.*.*(..)) " + 
	" && !@target(com.sjwi.catalog.aspect.IgnoreAspect)" +
	" && @target(org.springframework.stereotype.Controller))" + 
	" || execution(* com.sjwi.catalog.controller.song.*.*(..)) " +
	" || (execution(* com.sjwi.catalog.controller.setlist.*.*(..)) && !@target(com.sjwi.catalog.aspect.IgnoreAspect)) " +
	" || execution(* com.sjwi.catalog.controller.OrganizationController.organizationDetails(..)) " 
			)
	public void logPageRequest(JoinPoint joinPoint) {
		String signature = joinPoint.getSignature().toShortString();
		controllerHelper.logPageRequest(signature);
	}
	
}
