/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller.setlist;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;

@RestController("/setlists")
public class SetListDetailsController {

  public static final int SET_LISTS_PER_PAGE = 25;

  @Autowired ControllerHelper controllerHelper;

  @Autowired SetListService setListService;

  @Autowired OrganizationService organizationService;

  @Autowired SongService songService;

  @LandingPageAspect
  @RequestMapping(
      value = {"/{id}"},
      method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public ModelAndView setListDetails(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @RequestParam(name = "keys[]", required = false) List<String> keys,
      @PathVariable int id) {
    ModelAndView mv = new ModelAndView("setlist");
    try {
      SetList setList = setListService.getSetListById(id);
      if (keys != null) setList = setList.transpose(keys);
      mv.addObject("set", setList);
      mv.addObject("orgs", organizationService.getOrganizations());
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlists"},
      method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  @LandingPageAspect
  public ModelAndView setListPage(
      @RequestParam(name = "view", required = false) Optional<String> view,
      @RequestParam(name = "cursor", required = false) Integer cursor,
      HttpServletRequest request,
      Authentication auth,
      HttpServletResponse response) {
    ModelAndView mv = new ModelAndView(view.filter(v -> !v.trim().isEmpty()).orElse("setlists"));
    try {
      mv.addObject(
          "sets", setListService.getSetListPage(SET_LISTS_PER_PAGE, cursor == null ? 0 : cursor));
      mv.addObject("orgs", organizationService.getOrganizations());
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"/details/{id}"},
      method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public ModelAndView setListPartialDetails(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @PathVariable int id,
      @RequestParam(name = "view", required = true) String view,
      @RequestParam(name = "keys[]", required = false) List<String> keys) {
    ModelAndView mv = new ModelAndView(view);
    try {
      SetList setList = setListService.getSetListById(id);
      if (keys != null) setList = setList.transpose(keys);
      mv.addObject("set", setList);
      mv.addObject("songs", setList.getSongs());
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
    return mv;
  }

  @GetMapping("/")
  public ResponseEntity<List<SetList>> getOrganization(
      @RequestParam(required = false) Integer orgId) {
    if (orgId != null) return ResponseEntity.ok(setListService.getSetListsByOrg(orgId));
    return ResponseEntity.ok(setListService.getSetLists(SET_LISTS_PER_PAGE));
  }

  @GetMapping("/latest-set")
  public RedirectView siteLatestSet() {
    return new RedirectView("/setlists/" + setListService.getLatestSet().getId());
  }

  @RequestMapping(
      value = {"/search/setlist"},
      method = RequestMethod.GET)
  public ModelAndView searchSetList(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @RequestParam(value = "searchValue", required = false) String searchValue) {
    try {
      ModelAndView mv = null;
      if (searchValue == null || searchValue.isEmpty()) {
        mv = new ModelAndView("dynamic/set-list-table");
        mv.addObject("sets", setListService.getSetLists(10));
      } else {
        mv = new ModelAndView("dynamic/set-list-page");
        mv.addObject("sets", setListService.searchSetLists(searchValue));
      }
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }
}
