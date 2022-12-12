/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.SetListState;
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
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SearchController {

  @Autowired SongService songService;

  @Autowired SetListService setListService;

  @Autowired ControllerHelper controllerHelper;

  @Autowired UserService userService;

  @RequestMapping(
      value = {"/search/song"},
      method = RequestMethod.GET)
  public ModelAndView searchSong(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @RequestParam(value = "searchValue", required = false) String searchValue) {
    try {
      ModelAndView mv = new ModelAndView("dynamic/song-table");
      mv.addObject("songs", songService.searchSongs(searchValue));
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
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
      List<SetList> setlists;
      if (searchValue == null || searchValue.isEmpty()) {
        mv = new ModelAndView("dynamic/set-list-table");
        setlists = setListService.getSetLists(10);
      } else {
        mv = new ModelAndView("dynamic/set-list-page");
        setlists = setListService.searchSetLists(searchValue);
      }
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
      mv.addObject("setListStates", state);
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }
}
