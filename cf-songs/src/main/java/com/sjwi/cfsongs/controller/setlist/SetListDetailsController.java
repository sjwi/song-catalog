package com.sjwi.cfsongs.controller.setlist;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.cfsongs.aspect.LandingPageAspect;
import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.model.SetList;
import com.sjwi.cfsongs.service.SetListService;
import com.sjwi.cfsongs.service.SongService;

@Controller
public class SetListDetailsController {
	
	public static final int SET_LISTS_PER_PAGE = 10;
	
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	SetListService setListService;
	
	@Autowired
	SongService songService;
	
	@LandingPageAspect
	@RequestMapping(value = {"setlist/{id}"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView setListDetails(HttpServletRequest request, HttpServletResponse response,
			Authentication auth,
			@RequestParam(name="keys[]",required=false) List<String> keys,
			@PathVariable int id) throws IOException {
		ModelAndView mv = new ModelAndView("setlist");
		try {
			SetList setList = setListService.getSetListById(id);
			if (keys != null) {
				setList = setList.transpose(keys);
			}
			mv.addObject("set", setList);

		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return mv;
	}
	
	@RequestMapping(value = {"setlists"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	@LandingPageAspect
	public ModelAndView setListPage(
			@RequestParam(name = "view",required=false) Optional<String> view,
			@RequestParam(name = "additionalSets",required=false) boolean page,
			HttpServletRequest request, Authentication auth, HttpServletResponse response) throws IOException {
		ModelAndView mv = new ModelAndView(view.filter(v -> !v.trim().isEmpty()).orElse("setlists"));
		try {
			List<SetList> setLists = null;
			if (page) {
				setLists = setListService.getSetListPage(SET_LISTS_PER_PAGE);
			} else {
				setLists = setListService.getSetLists(SET_LISTS_PER_PAGE);
			}
			mv.addObject("sets", setLists);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return mv;
	}
	
	@RequestMapping(value = {"setlist/details/{id}"}, method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ModelAndView setListPartialDetails(HttpServletRequest request, HttpServletResponse response,
			Authentication auth,
			@PathVariable int id, @RequestParam(name="view",required=true) String view,
			@RequestParam(name="keys[]",required=false) List<String> keys) throws IOException {
		ModelAndView mv = new ModelAndView(view);
		try {
			SetList setList = setListService.getSetListById(id);
			if (keys != null) {
				setList = setList.transpose(keys);
			}
			mv.addObject("set", setList);
			mv.addObject("songs", setList.getSongs());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return mv;
	}
}
