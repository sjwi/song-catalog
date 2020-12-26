package com.sjwi.catalog.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.aspect.IgnoreAspect;
import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.Organization;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.UriUtils;

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
	
	@Autowired
	CustomLogger log;

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
	
	@LandingPageAspect
	@RequestMapping(value = {"/orgs"}, method = RequestMethod.GET)
	public ModelAndView organizations() throws IOException {
		ModelAndView mv = new ModelAndView("organizations");
		mv.addObject("frequentSongs",songService.getFrequencyCount());
		mv.addObject("orgs",organizationService.getOrganizations());
		mv.addObject("services",organizationService.getMeetingServices());
		mv.addObject("sets",setListService.getSetLists());
		return mv;
	}

	@LandingPageAspect
	@RequestMapping(value = {"/org/{id}"}, method = RequestMethod.GET)
	public ModelAndView organizationDetails(@PathVariable int id, Authentication auth) throws IOException {
		ModelAndView mv = new ModelAndView("organization");
		mv.addObject("sets",setListService.getSetListsByOrg(id));
		mv.addObject("frequentSongs",songService.getFrequencyCountByOrg(id));
		mv.addObject("org",organizationService.getOrganizationById(id));
		mv.addObject("orgs",organizationService.getOrganizations());
		mv.addObject("services",organizationService.getMeetingServices());
		log.logUserActionWithEmail("Organization Page " + id + " visited");
		return mv;
	}

	@RequestMapping(value = {"/org/{id}/song-frequency", "/orgs/song-frequency"}, method = RequestMethod.GET)
	public ModelAndView organizationSongFrequency(@PathVariable(required = false) Integer id, @RequestParam String view, @RequestParam(required = false) List<Integer> services, Authentication auth) throws IOException {
		ModelAndView mv = new ModelAndView(view);
		mv.addObject("frequentSongs", id==null? songService.getServiceFrequencyCount(services): songService.getServiceFrequencyCountByOrg(id,services));
		return mv;
	}

	@RequestMapping(value = {"/org/{id}/{endpoint}"}, method = RequestMethod.GET)
	public ModelAndView latestOrgResource(@PathVariable int id, @PathVariable String endpoint, HttpServletResponse response) throws IOException {
		SetList setList = setListService.getLatestSetByOrg(id);
		Organization organization = organizationService.getOrganizationById(id);
		String today = new SimpleDateFormat("MM-dd-yyyy").format(new Date());
		switch (endpoint) {
			case "lyrics-handout":
				return new ModelAndView("redirect:" + context.getContextPath() + "/org/" + id + "/" + organization.getName() + "%20Worship%20Handout");
			case "deck": case "slides": case "slideshow": case "ppt": case "presentation":
				return new ModelAndView("forward:/" + context.getContextPath() 
						+ "/setlist/ppt/" + setList.getId()
						+ "/" + UriUtils.encode(organization.getName() + " PowerPoint Deck " + today,"UTF-8"));
			case "set": case "setlist":
				return new ModelAndView("forward:/setlist/" + setListService.getLatestSetByOrg(id).getId());
			default:
				return new ModelAndView("forward:/" + context.getContextPath() 
						+ "/setlist/pdf/" + setList.getId()
						+ "/" + UriUtils.encode(organization.getName() + " Worship " + today,"UTF-8")
						+ "?lyricsOnly=true&fontSize=18");
		}
	}
}