package com.sjwi.cfsongs.controller;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

import com.sjwi.cfsongs.aspect.IgnoreAspect;
import com.sjwi.cfsongs.model.SetList;
import com.sjwi.cfsongs.service.OrganizationService;
import com.sjwi.cfsongs.service.SetListService;
import com.sjwi.cfsongs.service.SongService;

@Controller
@IgnoreAspect
public class OrganizationController {

	@Autowired
	OrganizationService organizationService;

	@Autowired
	SongService songService;
	
	@Autowired
	SetListService setListService;
	
	@Autowired
	ServletContext context;
	
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
	
	@RequestMapping(value = {"/org/{id}/{endpoint}","/org/{id}"}, method = RequestMethod.GET)
	public ModelAndView latestPdfByOrg(@PathVariable int id, @PathVariable(required = false) String endpoint, HttpServletResponse response) throws IOException {
		SetList setList = setListService.getLatestSetByOrg(id);
		switch (endpoint == null? "": endpoint) {
			case "deck": case "slides": case "slideshow": case "ppt": case "presentation":
				return new ModelAndView("forward:/" + context.getContextPath() 
						+ "/setlist/ppt/" + setList.getId()
						+ "/" + UriUtils.encode(setList.getNormalizedSetListName(),"UTF-8"));
			case "set": case "setlist":
				return new ModelAndView("forward:/setlist/" + setListService.getLatestSetByOrg(id).getId());
			default:
				return new ModelAndView("forward:/" + context.getContextPath() 
						+ "/setlist/pdf/" + setList.getId()
						+ "/" + UriUtils.encode(setList.getNormalizedSetListName(),"UTF-8")
						+ "?lyricsOnly=true&fontSize=18");
		}
	}
}