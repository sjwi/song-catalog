/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@ControllerAdvice
public class ExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired OrganizationService organizationService;

  @Autowired CustomLogger logger;

  @LandingPageAspect
  @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
  public ModelAndView exceptionHanlder(Exception e) {
    logger.logErrorWithEmail(e.getMessage());
    ModelAndView mv = new ModelAndView("error");
    mv.addObject("orgs", organizationService.getOrganizations());
    return mv;
  }
}
