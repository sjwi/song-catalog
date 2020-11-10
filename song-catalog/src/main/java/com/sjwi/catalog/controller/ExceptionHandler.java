package com.sjwi.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.service.OrganizationService;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

	@Autowired
	OrganizationService organizationService;
	
	@LandingPageAspect
	@org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
	public ModelAndView exceptionHanlder(Exception e) {
		e.printStackTrace();
		ModelAndView mv = new ModelAndView("error");
		mv.addObject("orgs",organizationService.getOrganizations());
		return mv;
	}
}
