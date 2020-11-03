package com.sjwi.catalog.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;

@Controller
public class HomeController {
	
	@Autowired
	SongService songService;
	
	@Autowired 
	SetListService setListService;
	
	@Autowired
	ControllerHelper controllerHelper;
	
	@LandingPageAspect
	@RequestMapping(value = { "/home", "/"})
	public ModelAndView home(HttpServletRequest request,
			HttpServletResponse response, Authentication auth, @RequestParam (name="searchTerm", required=false) String searchTerm) {
		ModelAndView mv = new ModelAndView("home");
		mv.addObject("songs",songService.searchSongs(searchTerm));
		mv.addObject("sets",setListService.getSetLists(10));
		mv.addObject("searchTerm",searchTerm);
		mv.addObject("categories",songService.getSongCategories());
		return mv;
	}
}
