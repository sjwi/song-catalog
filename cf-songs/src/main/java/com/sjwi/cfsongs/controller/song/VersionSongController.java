package com.sjwi.cfsongs.controller.song;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

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
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.TransposableString;
import com.sjwi.cfsongs.model.song.Song;
import com.sjwi.cfsongs.service.RecordingService;
import com.sjwi.cfsongs.service.SetListService;
import com.sjwi.cfsongs.service.SongService;
import com.sjwi.cfsongs.service.VersionService;

import static com.sjwi.cfsongs.model.KeySet.NUMBER_SYSTEM_KEY_CODE;

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
	CustomLogger logger;

	@RequestMapping(value = { "/song/version/{id}"}, method = RequestMethod.GET)
	public ModelAndView getSongDetailsForEdit(@PathVariable int id, @RequestParam(value="view", required = true) String view, HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ModelAndView mv = new ModelAndView(view);
		Song song = songService.getSongById(id);
		if (song.getRelated() != 0) {
			song = versionService.getVersionById(id);
		}
		mv.addObject("song",song);
		return mv;
	}

	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = { "/song/version/{id}"}, method = RequestMethod.POST)
	public void createNewVersion(@PathVariable int id, 
			@RequestParam(value = "songId", required = true) int songId,
			@RequestParam(value = "defaultVersionKey", required = true) String defaultKey,
			@RequestParam(value = "updatedVersionKey", required = true) String updatedVersionKey,
			@RequestParam(value = "versionBody", required = true) String versionBody,
			@RequestPart(value = "songAudio", required = false) Part songAudio,
			@RequestParam(value = "setSongId", required = false) int setSongId,
			Principal principal,Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		songId = versionService.createNewVersion(songId, principal.getName(), new TransposableString(versionBody, defaultKey).getTransposedString(NUMBER_SYSTEM_KEY_CODE), defaultKey);
		if (!defaultKey.equals(updatedVersionKey)) {
			songService.setDefaultKey(updatedVersionKey, songId, principal.getName());
		}
		if (setSongId != 0) {
			setListService.changeVersion(setSongId,songId);
		}
		if (songAudio != null) {
			recordingService.addOrUpdateRecording(songId, songAudio);
		}
		logger.logMessageWithEmail("New version created by " + auth.getName() + ": " + songId);
	}
	
	@RequestMapping(value = { "/song/get-all-related/{id}"}, method = RequestMethod.GET)
	public ModelAndView getAllRelatedSongs(@PathVariable int id, Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ModelAndView mv = new ModelAndView("modal/dynamic/change-version");
		mv.addObject("versions",versionService.getAllRelatedSongs(id));
		mv.addObject("currentVersionId",id);
		return mv;
	}
	
	@RequestMapping(value = { "/song/version/swap-master/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void swapMaster(@PathVariable int id, @RequestParam(value="newId",required = true) int newId, Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		versionService.changeMaster(newId, id);
	}
}
