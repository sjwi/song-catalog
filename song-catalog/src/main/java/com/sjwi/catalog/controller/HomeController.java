/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import static com.sjwi.catalog.controller.setlist.SetListDetailsController.SET_LISTS_PER_PAGE;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.SetListState;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class HomeController {

  @Autowired SongService songService;

  @Autowired SetListService setListService;

  @Autowired ControllerHelper controllerHelper;

  @Autowired OrganizationService organizationService;

  @Autowired UserService userService;

  @LandingPageAspect
  @RequestMapping(value = {"/home", "/"})
  public ModelAndView home(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @RequestParam(name = "searchTerm", required = false) String searchTerm) {
    try {
      ModelAndView mv = new ModelAndView("home");
      List<SetList> setlists = setListService.getSetLists(SET_LISTS_PER_PAGE);
      Map<Integer, SetListState> state = userService.getAllSetlistStatesForUser();
      setlists =
          setlists.stream()
              .map(
                  s -> {
                    if (state.containsKey(s.getId())) return s.transpose(state.get(s.getId()));
                    else return s;
                  })
              .collect(Collectors.toList());
      mv.addObject("sets", setlists);
      mv.addObject(
          "songs",
          searchTerm == null ? songService.getSongs() : songService.searchSongs(searchTerm));
      mv.addObject("setListStates", state);
      mv.addObject("orgs", organizationService.getOrganizations());
      mv.addObject("searchTerm", searchTerm);
      mv.addObject("categories", songService.getSongCategories());
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }
}
