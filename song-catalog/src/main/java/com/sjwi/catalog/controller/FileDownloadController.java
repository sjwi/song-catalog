package com.sjwi.catalog.controller;

import static com.sjwi.catalog.model.KeySet.LYRICS_ONLY_KEY_CODE;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.file.pdf.PdfFileGenerator;
import com.sjwi.catalog.file.ppt.PptFileGenerator;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.SongService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class FileDownloadController {
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	SongService songService;
	
	@Autowired
	SetListService setListService;
	
	@Autowired
	CustomLogger logger;
	
	@RequestMapping(value = {"{downloadType}/download/{id}"}, method = RequestMethod.GET)
	public ModelAndView downloadModal(@PathVariable String downloadType, @PathVariable int id,
			@RequestParam (name="key", required = false) String key) {
        try {
			ModelAndView mv = new ModelAndView("modal/dynamic/download");
			mv.addObject("defaultFileName",downloadType.equalsIgnoreCase("song")? 
				songService.getSongById(id).getNormalizedName() : setListService.getSetListById(id).getNormalizedSetListName());
			mv.addObject("key",key);
			return mv;
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
	}
	
	@RequestMapping(value = {"/exportDatabase"}, method = RequestMethod.GET)
	public ModelAndView exportModal() {
		try {
			ModelAndView mv = new ModelAndView("modal/dynamic/download");
			mv.addObject("defaultFileName","Song_Catalog_Export");
			return mv;
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = {"/exportDatabase/ppt/{fileName}"}, method = RequestMethod.GET)
	public void exportSongDatabaseAsPpt(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String fileName,
			@RequestParam (name="blankSlide", required = false) boolean prependBlankSlide,
			@RequestParam (name="alignCenter", required = false) boolean alignCenter,
			@RequestParam (name="blankSlideDelimiter", required = false) boolean blankSlideDelimiter,
			@RequestParam (name="fontSize") Optional<Integer> fontSize) {
		try {
			FileGenerator pptGenerator = new PptFileGenerator(prependBlankSlide,fontSize.orElse(0),alignCenter, blankSlideDelimiter);
			List<Song> songs = songService.getSongs().stream().map(s -> s.transpose(LYRICS_ONLY_KEY_CODE)).collect(Collectors.toList());
			String date = "_" + new SimpleDateFormat("MMddyyyy").format(new Date());
			fileName = fileName == null? "Song_Catalog_Export" + date: controllerHelper.normalizeString(fileName + date);
			response.addHeader("Content-Disposition", "attachment; filename=\""+ fileName + ".pptx\"");
			Files.copy(Paths.get(pptGenerator.buildFile(new SetList("",songs))), response.getOutputStream());
			logger.logUserActionWithEmail(fileName + " ppt downloaded." + "\n" + controllerHelper.getFullUrl());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = {"/exportDatabase/pdf/{fileName}"}, method = RequestMethod.GET)
	public void exportSongDatabaseAsPdf(HttpServletRequest request, HttpServletResponse response,
			@PathVariable String fileName,
			@RequestParam (name="fontSize") Optional<Integer> fontSize,
			@RequestParam (value = "lyricsOnly", required = false, defaultValue = "false") boolean lyricsOnly,
			@RequestParam (name="qrCode", required=false, defaultValue = "false") boolean qrCode) {
		try {
			FileGenerator pdfGenerator = new PdfFileGenerator(fontSize.orElse(0), qrCode? controllerHelper.getFullUrl(): null);
			List<Song> songs = songService.getSongs().stream()
									.map(s -> lyricsOnly? s.transpose(LYRICS_ONLY_KEY_CODE): s)
									.collect(Collectors.toList());
			String date = "_" + new SimpleDateFormat("MMddyyyy").format(new Date());
			fileName = fileName == null? "Song_Catalog_Export" + date: controllerHelper.normalizeString(fileName + date);
			pdfGenerator.buildFile(new SetList("",songs));
			response.setContentType("application/pdf; name=\"" + fileName + "\"");
            response.addHeader("Content-Disposition", "inline; filename=\"" + fileName + ".pdf\"");
			Files.copy(Paths.get(pdfGenerator.getFilePath()), response.getOutputStream());
			logger.logUserActionWithEmail(fileName + " pdf downloaded." + "\n" + controllerHelper.getFullUrl());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = {"/song/ppt/{id}/{fileName}"}, method = RequestMethod.GET)
	public void downloadSongPpt(HttpServletRequest request, HttpServletResponse response,
			@PathVariable int id,
			@PathVariable String fileName,
			@RequestParam (name="blankSlide", required = false) boolean prependBlankSlide,
			@RequestParam (name="blankSlideDelimiter", required = false) boolean blankSlideDelimiter,
			@RequestParam (name="alignCenter", required = false) boolean alignCenter,
			@RequestParam (name="fontSize") Optional<Integer> fontSize) {
		try {
			FileGenerator pptGenerator = new PptFileGenerator(prependBlankSlide,fontSize.orElse(0),alignCenter,blankSlideDelimiter);
			Song song = songService.getSongById(id).transpose(LYRICS_ONLY_KEY_CODE);
			fileName = fileName == null? song.getNormalizedName(): controllerHelper.normalizeString(fileName);
            response.addHeader("Content-Disposition", "attachment; filename=\""+ fileName + ".pptx\"");
            Files.copy(Paths.get(pptGenerator.buildFile(song)), response.getOutputStream());
			logger.logUserActionWithEmail(fileName + " ppt downloaded." + "\n" + controllerHelper.getFullUrl());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = {"/setlist/ppt/{id}/{fileName}", "/setlist/ppt/{id}"}, method = RequestMethod.GET)
	public void downloadSetPpt(HttpServletRequest request, HttpServletResponse response,
			@PathVariable (required = false) String fileName,
			@RequestParam (name="blankSlide", required = false) boolean prependBlankSlide,
			@RequestParam (name="blankSlideDelimiter", required = false) boolean blankSlideDelimiter,
			@RequestParam (name="alignCenter", required = false) boolean alignCenter,
			@RequestParam (name="fontSize") Optional<Integer> fontSize,
			@PathVariable int id) {
		try {
			FileGenerator pptGenerator = new PptFileGenerator(prependBlankSlide, fontSize.orElse(0),alignCenter,blankSlideDelimiter);
			SetList setList = controllerHelper.buildSetFile(id,pptGenerator,true);
			fileName = fileName == null? setList.getNormalizedSetListName(): controllerHelper.normalizeString(fileName);
            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + ".pptx\"");
            Files.copy(Paths.get(pptGenerator.getFilePath()), response.getOutputStream());
			logger.logUserActionWithEmail(fileName + " ppt downloaded." + "\n" + controllerHelper.getFullUrl());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
	
	@RequestMapping(value = {"/song/pdf/{id}/{fileName}"}, method = RequestMethod.GET)
	public void downloadSongPdf(HttpServletRequest request, HttpServletResponse response,
			Authentication auth,
			@PathVariable int id,
			@PathVariable (required = false) String fileName,
			@RequestParam (name="fontSize") Optional<Integer> fontSize,
			@RequestParam (value = "lyricsOnly", required = false, defaultValue = "false") boolean lyricsOnly,
			@RequestParam (name="qrCode", required=false, defaultValue = "false") boolean qrCode,
			@RequestParam (name="key",required=false) String key) {
		try {
			FileGenerator pdfGenerator = new PdfFileGenerator(fontSize.orElse(0), qrCode? controllerHelper.getFullUrl(): null);
			Song song = songService.getSongById(id);
			fileName = fileName == null? song.getNormalizedName(): controllerHelper.normalizeString(fileName);
			if (lyricsOnly)
				song = song.transpose(LYRICS_ONLY_KEY_CODE);
			else if (key != null)
				song = song.transpose(key);
			response.setContentType("application/pdf; name=\"" + fileName+ "\"");
            response.addHeader("Content-Disposition", "inline; filename=\""+ fileName + ".pdf\"");
            Files.copy(Paths.get(pdfGenerator.buildFile(song)), response.getOutputStream());
			logger.logUserActionWithEmail(fileName + " pdf downloaded." + "\n" + controllerHelper.getFullUrl());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = {"/setlist/pdf/{id}/{fileName}", "/setlist/pdf/{id}"}, method = RequestMethod.GET)
	public void downloadSetPdf(HttpServletRequest request, HttpServletResponse response,
			Authentication auth,
			@PathVariable int id,
			@PathVariable (required = false) String fileName,
			@RequestParam (name="fontSize") Optional<Integer> fontSize,
			@RequestParam(value = "lyricsOnly", required = false, defaultValue = "false") boolean lyricsOnly,
			@RequestParam (name="qrCode",required=false, defaultValue = "false") boolean qrCode) {
		try {
			FileGenerator pdfGenerator = new PdfFileGenerator(fontSize.orElse(0), qrCode? controllerHelper.getFullUrl(): null);
			SetList setList = lyricsOnly? 
								setListService.getLyricsToSetListById(id).setSetListName(fileName): 
								setListService.getSetListById(id).setSetListName(fileName);
			pdfGenerator.buildFile(setList);
			fileName = fileName == null? setList.getNormalizedSetListName(): controllerHelper.normalizeString(fileName);
			response.setContentType("application/pdf; name=\"" + fileName + "\"");
            response.addHeader("Content-Disposition", "inline; filename=\"" + fileName + ".pdf\"");
            Files.copy(Paths.get(pdfGenerator.getFilePath()), response.getOutputStream());
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
}
