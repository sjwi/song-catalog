package com.sjwi.cfsongs.controller.song;

import java.io.IOException;
import java.security.Principal;
import java.util.Date;

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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

import com.sjwi.cfsongs.controller.ControllerHelper;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.ResponseMessage;
import com.sjwi.cfsongs.model.TransposableString;
import com.sjwi.cfsongs.model.song.MasterSong;
import com.sjwi.cfsongs.model.song.Song;
import com.sjwi.cfsongs.service.RecordingService;
import com.sjwi.cfsongs.service.SetListService;
import com.sjwi.cfsongs.service.SongService;
import com.sjwi.cfsongs.service.VersionService;

@Controller
public class SongLifecycleController {
	@Autowired
	ControllerHelper controllerHelper;
	
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
	
	@RequestMapping(value = {"song/create"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage createSong(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "songTitle", required = true) String songTitle,
			@RequestParam(value = "chordedIn", required = true) String chordedIn,
			@RequestParam(value = "category", required = true) int category,
			@RequestPart(value = "songAudio", required = false) Part songAudio,
			Principal principal,Authentication auth,
			@RequestParam(value = "songBody", required = false) String songBody) throws IOException {
		try {
			int id = songService.addSong(controllerHelper.titleCase(songTitle),songBody,chordedIn,principal.getName(),category);
			if (songAudio != null) {
				recordingService.addOrUpdateRecording(id, songAudio);
			}
			logger.logMessageWithEmail("New song created by " + auth.getName() + ": " + songTitle);
			return new ResponseMessage("success",id);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
			return new ResponseMessage("failure","0");
		}
	}
	
	@RequestMapping(value = {"song/delete"}, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteSong(HttpServletRequest request, HttpServletResponse response,Authentication auth,
			@RequestParam(value = "songId", required = true) int songId) throws IOException {
		try {
			songService.deleteSong(songId);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
	}
	@RequestMapping(value = { "/song/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView getSongDetailsForEdit(@PathVariable int id,Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		ModelAndView mv = new ModelAndView("modal/dynamic/edit-song");
		Song song = songService.getSongById(id);
		mv.addObject("song",song);
		if (song.getRelated() != 0) {
			mv.addObject("versionName",versionService.getVersionById(song.getId()).getVersionName());
		}
		mv.addObject("categories",songService.getSongCategories());
		return mv;
	}
	@ResponseStatus(value = HttpStatus.OK)
	@RequestMapping(value = { "/song/edit/{id}"}, method = RequestMethod.POST)
	public void editSong(@PathVariable int id,
			@RequestParam(value = "songTitle", required = true) String songTitle,
			@RequestParam(value = "songBody", required = true) String songBody,
			@RequestParam(value = "defaultKey", required = true) String defaultKey,
			@RequestParam(value = "updatedKey", required = true) String updatedKey,
			@RequestParam(value = "newVersion", required = false) String newVersion,
			@RequestParam(value = "setSongId", required = false) int setSongId,
			@RequestParam(value = "category", required = true) int category,
			@RequestPart(value = "songAudio", required = false) Part songAudio,
			Principal principal,Authentication auth,
			HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		Song existingSong = songService.getSongById(id);
		Song updatedSong = new MasterSong(null, existingSong.getId(),songTitle,new TransposableString(songBody,defaultKey),existingSong.getFreq(),
				updatedKey,existingSong.getArtist(),existingSong.getNotes(),existingSong.getCreatedBy(),
				principal.getName(),new Date(), existingSong.getRelated(),existingSong.isPriv(),category,existingSong.getRecording());
		if (newVersion != null) {
			id = versionService.createNewVersion(updatedSong.getRelated() == 0? id: updatedSong.getRelated(), principal.getName(),
					updatedSong.getBody(), updatedSong.getDefaultKey());
		}
		else {
			songService.updateSong(updatedSong, principal.getName());
		}
		if (setSongId != 0 && newVersion != null) {
			setListService.changeVersion(setSongId, id);
		}
		if (songAudio != null) {
			recordingService.addOrUpdateRecording(id, songAudio);
		}
	}
	@RequestMapping(value = {"song/delete/recording/{id}"}, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteSongRecording(HttpServletRequest request, HttpServletResponse response,Authentication auth,
			@PathVariable int id) throws IOException {
		try {
			recordingService.deleteRecording(id);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
	}
	@RequestMapping(value = {"song/add/recording/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void addSongRecording(HttpServletRequest request, HttpServletResponse response,Authentication auth,
			@RequestPart(value = "file", required = false) Part songAudio,
			@PathVariable int id) throws IOException {
		try {
			recordingService.addOrUpdateRecording(id, songAudio);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			response.sendRedirect(request.getContextPath() + "/error");
		}
	}
}
