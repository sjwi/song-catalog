/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller.setlist;

import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.SetListState;
import com.sjwi.catalog.model.SetListState.SetSongSetting;
import com.sjwi.catalog.model.song.SetListSong;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.UserService;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SetListDetailsController {

  public static final int SET_LISTS_PER_PAGE = 25;

  @Autowired ControllerHelper controllerHelper;

  @Autowired SetListService setListService;

  @Autowired OrganizationService organizationService;

  @Autowired SongService songService;

  @Autowired UserService userService;

  @Autowired CustomLogger logger;

  @LandingPageAspect
  @RequestMapping(
      value = {"setlist/{id}"},
      method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public ModelAndView setListDetails(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @PathVariable int id) {
    ModelAndView mv = new ModelAndView("setlist");
    try {
      SetList setList = setListService.getSetListById(id);
      SetListState setListState = userService.getSetState(id);
      setList = setList.transpose(setListState);
      mv.addObject("set", setList);
      mv.addObject("setListState", setListState);
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
      List<SetList> setlists =
          setListService.getSetListPage(SET_LISTS_PER_PAGE, cursor == null ? 0 : cursor);
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
      mv.addObject("orgs", organizationService.getOrganizations());
      mv.addObject("setListStates", state);
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/details/{id}"},
      method = RequestMethod.GET)
  @ResponseStatus(value = HttpStatus.OK)
  public ModelAndView setListPartialDetails(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @PathVariable int id,
      @RequestParam(name = "view", required = true) String view) {
    ModelAndView mv = new ModelAndView(view);
    try {
      SetList setList = setListService.getSetListById(id);
      SetListState setListState = userService.getSetState(id);
      setList = setList.transpose(setListState);
      mv.addObject("set", setList);
      mv.addObject("setListState", setListState);
      mv.addObject("songs", setList.getSongs());
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
    return mv;
  }

  @RequestMapping(
      value = {"setlist/state/{id}"},
      method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.OK)
  public void updateSetSettings(
      @PathVariable Integer id, @RequestBody Map.Entry<Integer, SetSongSetting> body) {
    logger.logUserActionWithEmail("Setlist state set");
    SetList sl = setListService.getSetListById(id);
    String defKey =
        sl.getSongs().stream()
            .filter(s -> ((SetListSong) s).getSetListSongId() == body.getKey())
            .findAny()
            .get()
            .getDefaultKey();
    if (defKey.equals(body.getValue().getKey().toString())) userService.removeSetState(id);
    else userService.updateSetState(id, body.getKey(), body.getValue());
  }
}
