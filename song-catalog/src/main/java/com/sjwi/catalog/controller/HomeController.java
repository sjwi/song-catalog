package com.sjwi.catalog.controller;

import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {
	
	@Autowired
	SongService songService;
	
	@Autowired 
	SetListService setListService;
	
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	OrganizationService organizationService;

	@LandingPageAspect
	@RequestMapping(value = { "/home", "/"})
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response, Authentication auth, @RequestParam (name="searchTerm", required=false) String searchTerm) {
		try {
			ModelAndView mv = new ModelAndView("home");
			System.out.println("get songs: " + new Date());
			mv.addObject("songs", searchTerm == null? songService.getSongs(): songService.searchSongs(searchTerm));
			System.out.println("get orgs: " + new Date());
			mv.addObject("orgs",organizationService.getOrganizations());
			System.out.println("get sets: " + new Date());
			mv.addObject("sets",setListService.getSetLists(10));
			mv.addObject("searchTerm",searchTerm);
			System.out.println("get cats:" + new Date());
			mv.addObject("categories",songService.getSongCategories());
			return mv;
		} catch (Exception e) {
			return controllerHelper.errorHandler(e);
		}
	}
}
