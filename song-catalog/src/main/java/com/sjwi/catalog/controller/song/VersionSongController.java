package com.sjwi.catalog.controller.song;

import static com.sjwi.catalog.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.mail.MailConstants;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.VersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class VersionSongController {
	
	@Autowired
	SongService songService;
	
	@Autowired
	VersionService versionService;

	@Autowired
	SetListService setListService;
	
	@Autowired
	RecordingService recordingService;
	
	@Autowired
	Environment environment;

	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	CustomLogger logger;

	@RequestMapping(value = { "/song/version/{id}"}, method = RequestMethod.GET)
	public ModelAndView getSongDetailsForEdit(@PathVariable int id, @RequestParam(value="view", required = true) String view, HttpServletRequest request, HttpServletResponse response) {
		try {
			ModelAndView mv = new ModelAndView(view);
			Song song = songService.getSongById(id);
			if (song.getRelated() != 0) {
				song = versionService.getVersionById(id);
			}
			mv.addObject("song",song);
			return mv;
		} catch (Exception e) {
			return controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = { "/song/version/{id}"}, method = RequestMethod.POST)
	@ResponseBody
	public Integer createNewVersion(@PathVariable int id, 
			@RequestParam(value = "songId", required = true) int songId,
			@RequestParam(value = "defaultVersionKey", required = true) String defaultKey,
			@RequestParam(value = "updatedVersionKey", required = true) String updatedVersionKey,
			@RequestParam(value = "versionBody", required = true) String versionBody,
			@RequestPart(value = "songAudio", required = false) Part songAudio,
			@RequestParam(value = "setSongId", required = false) int setSongId,
			Principal principal,Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			int versionId = versionService.createNewVersion(songId, principal.getName(), new TransposableString(versionBody, defaultKey).getTransposedString(NUMBER_SYSTEM_KEY_CODE), defaultKey);
			if (!defaultKey.equals(updatedVersionKey))
				songService.setDefaultKey(updatedVersionKey, versionId, principal.getName());
			if (setSongId != 0)
				setListService.changeVersion(setSongId,versionId);
			if (songAudio != null)
				recordingService.addOrUpdateRecording(versionId, songAudio);
			logger.mailRevisionDeltas(songService.getSongById(songId), songService.getSongById(versionId), MailConstants.VERSION_CREATED_ACTION);
			return versionId;
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			throw e;
		} 
	}
	
	@RequestMapping(value = { "/song/get-all-related/{id}"}, method = RequestMethod.GET)
	public ModelAndView getAllRelatedSongs(@PathVariable int id, Authentication auth,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ModelAndView mv = new ModelAndView("modal/dynamic/change-version");
			mv.addObject("versions",versionService.getAllRelatedSongs(id));
			mv.addObject("currentVersionId",id);
			return mv;
		} catch (Exception e) {
			return controllerHelper.errorHandler(e);
		} 
	}
	
	@RequestMapping(value = { "/song/version/swap-master/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void swapMaster(@PathVariable int id, @RequestParam(value="newId",required = true) int newId, Authentication auth,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			versionService.changeMaster(newId, id);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
}
