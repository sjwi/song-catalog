package com.sjwi.catalog.controller.song;

import java.security.Principal;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.mail.MailConstants;
import com.sjwi.catalog.model.ResponseMessage;
import com.sjwi.catalog.model.TransposableString;
import com.sjwi.catalog.model.song.MasterSong;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.RecordingService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;
import com.sjwi.catalog.service.VersionService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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

	@Value("${com.sjwi.settings.app.baseUrl}")
	String baseUrl;
	
	@RequestMapping(value = {"song/create"}, method = RequestMethod.POST)
	@ResponseBody
	public ResponseMessage createSong(HttpServletRequest request, HttpServletResponse response,
			@RequestParam(value = "songTitle", required = true) String songTitle,
			@RequestParam(value = "chordedIn", required = true) String chordedIn,
			@RequestParam(value = "category", required = true) int category,
			@RequestPart(value = "songAudio", required = false) Part songAudio,
			Principal principal,Authentication auth,
			@RequestParam(value = "songBody", required = false) String songBody) {
		try {
			int id = songService.addSong(controllerHelper.titleCase(songTitle),songBody,chordedIn,principal.getName(),category);
			if (songAudio != null) {
				recordingService.addOrUpdateRecording(id, songAudio);
			}
			logger.logMessageWithEmail("New song created by " + auth.getName() + ": " + songTitle + "\n " + baseUrl + "/song/" + id);
			return new ResponseMessage("success",id);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
			return new ResponseMessage("failure","0");
		}
	}
	
	@RequestMapping(value = {"song/delete"}, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteSong(HttpServletRequest request, HttpServletResponse response,Authentication auth,
			@RequestParam(value = "songId", required = true) int songId) {
		try {
			Song song = songService.getSongById(songId);
			songService.deleteSong(songId);
			logger.logMessageWithEmail("Song deleted by " + auth.getName() + ": " + song.getNormalizedName() + " (ID: " + songId + ")");
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = { "/song/edit/{id}"}, method = RequestMethod.GET)
	public ModelAndView getSongDetailsForEdit(@PathVariable int id,Authentication auth,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			ModelAndView mv = new ModelAndView("modal/dynamic/edit-song");
			Song song = songService.getSongById(id);
			mv.addObject("song",song);
			if (song.getRelated() != 0) {
				mv.addObject("versionName",versionService.getVersionById(song.getId()).getVersionName());
			}
			mv.addObject("categories",songService.getSongCategories());
			return mv;
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
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
			HttpServletRequest request, HttpServletResponse response) {
		try {
			Song originalSong = songService.getSongById(id);
			Song revisedSong = new MasterSong(null, originalSong.getId(),songTitle,new TransposableString(songBody,defaultKey),
					updatedKey,originalSong.getArtist(),originalSong.getNotes(),originalSong.getCreatedBy(),
					principal.getName(),new Date(), originalSong.getRelated(),originalSong.isPriv(),category,originalSong.getRecording());
			if (newVersion != null)
				id = versionService.createNewVersion(revisedSong.getRelated() == 0? id: revisedSong.getRelated(), principal.getName(),
						revisedSong.getBody(), revisedSong.getDefaultKey());
			else
				songService.updateSong(revisedSong, principal.getName());
			if (setSongId != 0 && newVersion != null)
				setListService.changeVersion(setSongId, id);
			if (songAudio != null)
				recordingService.addOrUpdateRecording(id, songAudio);
			logger.mailRevisionDeltas(originalSong, revisedSong, MailConstants.EDIT_ACTION);
		} catch (Exception e){
			controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = {"song/delete/recording/{id}"}, method = RequestMethod.DELETE)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteSongRecording(HttpServletRequest request, HttpServletResponse response,Authentication auth,
			@PathVariable int id) {
		try {
			recordingService.deleteRecording(id);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = {"song/add/recording/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void addSongRecording(HttpServletRequest request, HttpServletResponse response,Authentication auth,
			@RequestPart(value = "file", required = false) Part songAudio,
			@PathVariable int id) {
		try {
			recordingService.addOrUpdateRecording(id, songAudio);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
}
