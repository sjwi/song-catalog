package com.sjwi.cfsongs.aspect;

import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.user.CfUser;

@Aspect
@Configuration
public class PageRequestLogger {

	@Autowired
	CustomLogger log;
	
	@Autowired
	ControllerHelper controllerHelper;

	@Before("(execution(* com.sjwi.cfsongs.controller.*.*(..)) " + 
	" && !@target(com.sjwi.cfsongs.aspect.IgnoreAspect)" +
	" && @target(org.springframework.stereotype.Controller))" + 
	" || execution(* com.sjwi.cfsongs.controller.song.*.*(..)) " +
	" || execution(* com.sjwi.cfsongs.controller.setlist.*.*(..)) " 
			)
	public void logPageRequest(JoinPoint joinPoint) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		
		String signature = joinPoint.getSignature().toShortString();
		String requestUrl = request.getRequestURL().toString();
		String parameters = getParametersAsFormattedString(request.getParameterMap());
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
	
	private String getParametersAsFormattedString(Map<String,String[]> parameterMap) {
		String parameters = parameterMap.entrySet().stream().map(p -> {
            String params = "\n\t\t" + p.getKey() + ": ";
            String[] paramValues = parameterMap.get(p.getKey());
            for (int i = 0; i < paramValues.length; i++) {
            	if (i == 0) {
					params = params +  paramValues[i];
            	} else {
					params = params + "," + paramValues[i];
            	}
            }
            return params;
		}).collect(Collectors.joining());
        return parameters;
	}
}
