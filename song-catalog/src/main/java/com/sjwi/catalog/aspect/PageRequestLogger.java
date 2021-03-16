package com.sjwi.catalog.aspect;

import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.user.CfUser;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Aspect
@Configuration
public class PageRequestLogger {

	@Autowired
	CustomLogger log;
	
	@Autowired
	ControllerHelper controllerHelper;

	@Before("(execution(* com.sjwi.catalog.controller.*.*(..)) " + 
	" && !@target(com.sjwi.catalog.aspect.IgnoreAspect)" +
	" && @target(org.springframework.stereotype.Controller))" + 
	" || execution(* com.sjwi.catalog.controller.song.*.*(..)) " +
	" || (execution(* com.sjwi.catalog.controller.setlist.*.*(..)) && !@target(com.sjwi.catalog.aspect.IgnoreAspect)) " +
	" || execution(* com.sjwi.catalog.controller.OrganizationController.organizationDetails(..)) " 
			)
	public void logPageRequest(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String signature = joinPoint.getSignature().toShortString();
		String requestUrl = request.getRequestURL().toString();
		String parameters = request.getParameterMap().entrySet().stream()
				.map(p -> "\n\t\t" + p.getKey() + ": " + String.join(",", request.getParameterMap().get(p.getKey()))).collect(Collectors.joining());
		String username = getSessionUser(joinPoint.getArgs());
		String os =  controllerHelper.getOs(request);

		log.info(signature + "\n\t" + requestUrl + parameters + "\n\tcalled by " + username + " on a " + os + " device.\n\n");
	}
	
	private String getSessionUser(Object[] objects) {
		try {
			return ((CfUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		} catch (Exception e) {
			return "anonymousUser";
		}
	}
}
