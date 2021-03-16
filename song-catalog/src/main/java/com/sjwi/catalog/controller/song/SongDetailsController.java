package com.sjwi.catalog.controller.song;

import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.VersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SongDetailsController {
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired 
	SongService songService;
	
	@Autowired
	SetListService setListService;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	OrganizationService organizationService;
	
	@LandingPageAspect
	@RequestMapping(value = { "/songs"}, method = RequestMethod.GET)
	public ModelAndView songCatalog(HttpServletRequest request, HttpServletResponse response, 
			Authentication auth, @RequestParam(name="searchTerm",required = false) String searchTerm) {
		try {
			List<Song> songs = songService.searchSongs(searchTerm);
			if (songs.size() == 0)
				return new ModelAndView("redirect:" + request.getContextPath() + "/?searchTerm=" + searchTerm);
			else if (songs.size() == 1)
				return new ModelAndView("redirect:" + request.getContextPath() + "/song/" + songs.get(0).getId());
			else {
				ModelAndView mv = searchTerm == null? new ModelAndView("songs"): new ModelAndView("home");
				mv.addObject("songs",songs);
				mv.addObject("sets",setListService.getSetLists(10));
				mv.addObject("focusedSong", versionService.getAllRelatedSongs(songs.get(0).getId()));
				mv.addObject("categories",songService.getSongCategories());
				mv.addObject("searchTerm",searchTerm);
				mv.addObject("orgs",organizationService.getOrganizations());
				return mv;
			}
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
	}
	
	@RequestMapping(value = { "/song/details/{id}"}, method = RequestMethod.GET)
	public ModelAndView songDetails(@PathVariable int id,
			@RequestParam(name="view",required=true) String view,
			@RequestParam(name="key",required=false) String key,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ModelAndView mv = new ModelAndView(view);
			Song song = songService.getSongById(id);
			if (key != null)
				song = song.transpose(key);
			mv.addObject("song", song);
			mv.addObject("sets", setListService.getSetLists(10));
			return mv;
		} catch (Exception e) {
			return controllerHelper.errorHandler(e);
		}
	}
	
	@LandingPageAspect
	@RequestMapping(value = { "/song/{id}"}, method = RequestMethod.GET)
	public ModelAndView songDetailsPage(
			@PathVariable int id, 
			@RequestParam(name="key",required=false) String key, 
			@RequestParam(name="view",required=false) String view, 
			HttpServletRequest request, HttpServletResponse response,Authentication auth) {

		try {
			ModelAndView mv = new ModelAndView(view==null?"songs":view);
			List<Song> songs = songService.getSongs();
			List<Song> focusedSong = versionService.getAllRelatedSongs(id);
			if (key != null) {
				focusedSong = focusedSong.stream().map(s -> {
					if (s.getId() == id)
						s = s.transpose(key);
					return s;
				}).collect(Collectors.toList());
			}
			mv.addObject("songs",songs);
			mv.addObject("sets",setListService.getSetLists(10));
			mv.addObject("focusedSong", focusedSong);
			mv.addObject("orgs",organizationService.getOrganizations());
			return mv;
		} catch (Exception e) {
			return controllerHelper.errorHandler(e);
		}
	}
}
