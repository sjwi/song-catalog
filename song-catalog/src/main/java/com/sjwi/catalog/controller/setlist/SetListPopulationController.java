/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller.setlist;

import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.UserService;
import com.sjwi.catalog.service.VersionService;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class SetListPopulationController {
  @Autowired ControllerHelper controllerHelper;

  @Autowired SongService songService;

  @Autowired SetListService setListService;

  @Autowired VersionService versionService;

  @Autowired CustomLogger logger;

  @Autowired UserService userService;

  @RequestMapping(
      value = {"setlist/add-song"},
      method = RequestMethod.POST)
  public ModelAndView addSongToSet(
      HttpServletRequest request,
      HttpServletResponse response,
      Principal principal,
      Authentication auth,
      @RequestParam(value = "song", required = true) int songId,
      @RequestParam(value = "version", required = false) String version,
      @RequestParam(value = "unit", required = false) Integer unit,
      @RequestParam(value = "subUnit", required = false) Integer subUnit,
      @RequestParam(value = "otherUnit", required = false) String otherUnit,
      @RequestParam(value = "otherSubUnit", required = false) String otherSubUnit,
      @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
          Date date,
      @RequestParam(value = "setListKey", required = false) String key,
      @RequestParam(value = "updatedVersionKey", required = false) String updatedVersionKey,
      @RequestParam(value = "versionBody", required = false) String versionBody,
      @RequestParam(value = "sort", required = false, defaultValue = "0") int sort,
      @RequestParam(value = "homegroupName", required = false) Integer homegroupName,
      @RequestParam(value = "otherGroupName", required = false) String otherGroupName,
      @RequestParam(value = "setList", required = true) int setListId) {
    try {
      if (setListId == 0) {
        String setListName =
            controllerHelper.buildSetlistName(
                unit, subUnit, otherUnit, otherSubUnit, date, homegroupName, otherGroupName);
        setListId = setListService.createSet(setListName, principal.getName(), unit, subUnit);
      }
      if (!"master".equals(version) && version != null) {
        if (!"newVersion".equals(version)) {
          songId = Integer.parseInt(version);
        } else {
          songId =
              versionService.createNewVersion(
                  songId,
                  principal.getName(),
                  new TransposableString(versionBody, key)
                      .getTransposedString(NUMBER_SYSTEM_KEY_CODE),
                  key);
          if (!key.equals(updatedVersionKey)) {
            songService.setDefaultKey(updatedVersionKey, songId, principal.getName());
          }
        }
      }
      setListService.addSongToSet(songId, setListId, key, sort);
      ModelAndView mv = new ModelAndView("dynamic/set-list-container");
      mv.addObject("set", setListService.getSetListById(setListId));
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/add-songs"},
      method = RequestMethod.POST)
  public ModelAndView addSongsToSet(
      HttpServletRequest request,
      HttpServletResponse response,
      Principal principal,
      Authentication auth,
      @RequestParam(value = "songs", required = true) List<Integer> songIds,
      @RequestParam(value = "unit", required = false) Integer unit,
      @RequestParam(value = "subUnit", required = false) Integer subUnit,
      @RequestParam(value = "otherUnit", required = false) String otherUnit,
      @RequestParam(value = "otherSubUnit", required = false) String otherSubUnit,
      @RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd")
          Date date,
      @RequestParam(value = "homegroupName", required = false) int homegroupName,
      @RequestParam(value = "otherGroupName", required = false) String otherGroupName,
      @RequestParam(value = "setList", required = true) int setListId) {
    try {
      if (setListId == 0) {
        String setListName =
            controllerHelper.buildSetlistName(
                unit, subUnit, otherUnit, otherSubUnit, date, homegroupName, otherGroupName);
        setListId = setListService.createSet(setListName, principal.getName(), unit, subUnit);
      }
      setListService.addSongsToSet(songIds, setListId);
      ModelAndView mv = new ModelAndView("dynamic/set-list-container");
      mv.addObject("set", setListService.getSetListById(setListId));
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/remove-song"},
      method = RequestMethod.POST)
  public ModelAndView removeSongFromSet(
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth,
      @RequestParam(value = "song", required = true) int songId,
      @RequestParam(value = "setList", required = true) int setListId) {
    try {
      setListService.removeSongFromSet(setListId, songId);
      ModelAndView mv = new ModelAndView("dynamic/set-list-container");
      mv.addObject("set", setListService.getSetListById(setListId));
      return mv;

    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/add-multiple-songs/{setId}"},
      method = RequestMethod.GET)
  public ModelAndView getSongsForSelect(
      @PathVariable int setId,
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth) {
    try {
      ModelAndView mv = new ModelAndView("modal/dynamic/add-song-from-select");
      mv.addObject("songs", songService.getSongs());
      mv.addObject("setId", setId);
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/add-multiple-songs"},
      method = RequestMethod.POST)
  public void addSongsToSetFromSelect(
      @RequestParam(value = "setId", required = true) int setId,
      @RequestParam(value = "songsToAdd", required = true) List<Integer> songsToAdd,
      HttpServletRequest request,
      HttpServletResponse response,
      Authentication auth) {
    try {
      setListService.addSongsToSet(songsToAdd, setId);
    } catch (Exception e) {
      controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/change-version"},
      method = RequestMethod.POST)
  public ModelAndView changeVersion(
      Authentication auth,
      @RequestParam(value = "setId", required = true) int setId,
      @RequestParam(value = "currentVersion", required = true) int oldVersion,
      @RequestParam(value = "version", required = true) int newVersion,
      HttpServletRequest request,
      HttpServletResponse response) {
    try {
      setListService.changeVersion(setId, oldVersion, newVersion);
      ModelAndView mv = new ModelAndView("dynamic/set-list-container");
      mv.addObject("set", setListService.getSetListById(setId));
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/change-key"},
      method = RequestMethod.POST)
  public void changeKey(
      @RequestParam(value = "defaultKey", required = true) String newKey,
      @RequestParam(value = "songId", required = true) int songId,
      Authentication auth,
      HttpServletRequest request,
      HttpServletResponse response) {
    try {
      setListService.setDefaultSetKey(newKey, songId);
      Integer setListId = setListService.getSetListIdBySong(songId);
      userService.removeSetState(setListId, songId);
    } catch (Exception e) {
      controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/sort/{id}"},
      method = RequestMethod.POST)
  public ModelAndView sortSet(
      HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable int id,
      Authentication auth,
      @RequestParam(name = "sortedSongs[]", required = true) List<Integer> sortedSongIds) {
    try {
      ModelAndView mv = new ModelAndView("dynamic/song-container");
      setListService.sortSetList(sortedSongIds);
      mv.addObject("songs", setListService.getSetSongs(id));
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"setlist/pin-latest/{id}"},
      method = RequestMethod.POST)
  @ResponseStatus(value = HttpStatus.OK)
  public void pinLatestSetList(
      HttpServletRequest request,
      HttpServletResponse response,
      @PathVariable int id,
      Authentication auth) {
    try {
      setListService.pinLatestSetList(id);
    } catch (Exception e) {
      controllerHelper.errorHandler(e);
    }
  }
}
