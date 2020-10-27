package com.sjwi.cfsongs.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.cfsongs.aspect.IgnoreAspect;
import com.sjwi.cfsongs.service.OrganizationService;
import com.sjwi.cfsongs.service.SongService;

@Controller
@IgnoreAspect
public class OrganizationController {

	@Autowired
	OrganizationService organizationService;

	@Autowired
	SongService songService;
	
	@RequestMapping(value = {"/organizations"}, method = RequestMethod.GET)
	public ModelAndView getOrganizations(Authentication auth, HttpServletRequest request,
			@RequestParam(name="view",required=true) String view) {
		ModelAndView mv = new ModelAndView(view);
		mv.addObject("organizations",organizationService.getOrganizations());
		return mv;
	}
	
	@RequestMapping(value = {"/services"}, method = RequestMethod.GET)
	public ModelAndView getServices(Authentication auth, HttpServletRequest request,
			@RequestParam(name="view",required=true) String view) {
		ModelAndView mv = new ModelAndView(view);
		mv.addObject("services",organizationService.getMeetingServices());
		return mv;
	}

	@RequestMapping(value = {"/categories"}, method = RequestMethod.GET)
	public ModelAndView getCategories(Authentication auth, HttpServletRequest request,
			@RequestParam(name="view",required=true) String view) {
		ModelAndView mv = new ModelAndView(view);
		mv.addObject("categories",songService.getSongCategories());
		return mv;
	}
}