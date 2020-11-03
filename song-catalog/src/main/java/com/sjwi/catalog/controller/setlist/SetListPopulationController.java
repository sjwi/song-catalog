package com.sjwi.catalog.controller.setlist;

import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.VersionService;

@Controller
public class SetListPopulationController {
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired 
	SongService songService;
	
	@Autowired 
	SetListService setListService;
	
	@Autowired
	VersionService versionService;
	
	@Autowired
	CustomLogger logger;
	
	@RequestMapping(value = {"setlist/add-song"}, method = RequestMethod.POST)
	public String addSongToSet(HttpServletRequest request, HttpServletResponse response, Principal principal, Authentication auth,
			@RequestParam(value = "song", required = true) int songId,
			@RequestParam(value = "version", required = false) String version,
			@RequestParam(value = "unit", required = false) Integer unit,
			@RequestParam(value = "subUnit", required = false) Integer subUnit,
			@RequestParam(value = "otherUnit", required = false) String otherUnit,
			@RequestParam(value = "otherSubUnit", required = false) String otherSubUnit,
			@RequestParam(value = "date", required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			@RequestParam(value = "setListKey", required = false) String key,
			@RequestParam(value = "updatedVersionKey", required = false) String updatedVersionKey,
			@RequestParam(value = "versionBody", required = false) String versionBody,
			@RequestParam(value = "sort", required = false, defaultValue="0") int sort,
			@RequestParam(value = "homegroupName", required = false) String homegroupName,
			@RequestParam(value = "setList", required = true) int setListId) throws IOException {
		try {
			if(setListId == 0) {
				String setListName = controllerHelper.buildSetlistName(unit,subUnit,otherUnit,otherSubUnit,date,homegroupName);
				setListId = setListService.createSet(setListName,principal.getName(), unit);
			}
			if (!"master".equals(version) && version != null) {
				if(!"newVersion".equals(version)) {
					songId = Integer.parseInt(version);
				} else {
					songId = versionService.createNewVersion(songId, principal.getName(), new TransposableString(versionBody,key).getTransposedString(NUMBER_SYSTEM_KEY_CODE), key);
					if (!key.equals(updatedVersionKey)) {
						songService.setDefaultKey(updatedVersionKey, songId, principal.getName());
					}
				}
			} 
			setListService.addSongToSet(songId, setListId, key, sort);
			request.setAttribute("set", setListService.getSetListById(setListId));

		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return "dynamic/set-list-container";
	}

	@RequestMapping(value = {"setlist/remove-song"}, method = RequestMethod.POST)
	public String removeSongFromSet(HttpServletRequest request, HttpServletResponse response,
			Authentication auth,
			@RequestParam(value = "song", required = true) int songId,
			@RequestParam(value = "setList", required = true) int setListId) throws IOException {
		try {
			setListService.removeSongFromSet(setListId, songId);
			request.setAttribute("set", setListService.getSetListById(setListId));

		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return "dynamic/set-list-container";
	}
	@RequestMapping(value = {"setlist/add-multiple-songs/{setId}"}, method = RequestMethod.GET)
	public String getSongsForSelect(@PathVariable int setId, HttpServletRequest request, HttpServletResponse response,
			Authentication auth) throws IOException {
		try {
			request.setAttribute("songs",songService.getSongs());
			request.setAttribute("setId",setId);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return "modal/dynamic/add-song-from-select";
	}	
	@RequestMapping(value = {"setlist/add-multiple-songs"}, method = RequestMethod.POST)
	public void addSongsToSetFromSelect(
			@RequestParam(value="setId", required=true) int setId,
			@RequestParam(value="songsToAdd", required=true) List<Integer> songsToAdd,
			HttpServletRequest request, HttpServletResponse response, Authentication auth) throws IOException {
		try {
			setListService.addSongsToSet(songsToAdd,setId);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
	}	
	@RequestMapping(value = {"setlist/change-version"}, method = RequestMethod.POST)
	public String changeVersion(Authentication auth,
			@RequestParam(value="setId", required=true) int setId,
			@RequestParam(value="currentVersion", required=true) int oldVersion,
			@RequestParam(value="version", required=true) int newVersion,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		setListService.changeVersion(setId,oldVersion,newVersion);
		request.setAttribute("set", setListService.getSetListById(setId));
		return "dynamic/set-list-container";
	}	
	@RequestMapping(value = {"setlist/change-key"}, method = RequestMethod.POST)
	public void changeKey(
			@RequestParam(value="defaultKey", required=true) String newKey,
			@RequestParam(value="songId", required=true) int songId,
			Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		setListService.setDefaultSetKey(newKey, songId);
	}	

	@RequestMapping(value = {"setlist/sort/{id}"}, method = RequestMethod.POST)
	public ModelAndView sortSet(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int id, Authentication auth,
			@RequestParam(name="sortedSongs[]",required=true) List<Integer> sortedSongIds) throws IOException {
		ModelAndView mv = new ModelAndView("dynamic/song-container");
		try {
			setListService.sortSetList(sortedSongIds);
			request.setAttribute("songs", setListService.getSetSongs(id));
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
		return mv;
	}
}
