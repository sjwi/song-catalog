package com.sjwi.catalog.controller.setlist;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import java.security.Principal;
import java.util.Date;

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

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;

@Controller
public class SetListLifecycleController {
	@Autowired
	ControllerHelper controllerHelper;
	
	@Autowired
	SetListService setListService;
	
	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	CustomLogger logger;
	
	@RequestMapping(value = {"setlist/create"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void createSet(
			@RequestParam(value = "unit", required = true) int unit,
			@RequestParam(value = "subUnit", required = false) int subUnit,
			@RequestParam(value = "otherUnit", required = false) String otherUnit,
			@RequestParam(value = "otherSubUnit", required = false) String otherSubUnit,
			@RequestParam(value = "homegroupName", required = false) int homegroupName,
			@RequestParam(value = "otherGroupName", required = false) String otherGroupName,
			@RequestParam("date") @DateTimeFormat(pattern = "yyyy-MM-dd") Date date,
			Principal principal, Authentication auth,
			HttpServletRequest request, HttpServletResponse response) {
		try {
			String setListName = controllerHelper.buildSetlistName(unit,subUnit,otherUnit,otherSubUnit,date,homegroupName,otherGroupName);
			setListService.createSet(setListName,principal.getName(), unit,subUnit);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
	@RequestMapping(value = {"setlist/delete/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void deleteSet(@PathVariable int id, Authentication auth, HttpServletRequest request, HttpServletResponse response) {
		try {
			SetList setList = setListService.getSetListById(id);
			setListService.deleteSet(id);
			logger.logMessageWithEmail("Set List deleted by " + auth.getName() + ": " + setList.getNormalizedSetListName() + " (ID: " + id + ")");
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = {"setlist/edit/{id}"}, method = RequestMethod.POST)
	@ResponseStatus(value = HttpStatus.OK)
	public void renameSet(@PathVariable int id, @RequestParam(required=true ,name="setListName") String setListName, Authentication auth, HttpServletRequest request, HttpServletResponse response) {
		try {
			setListService.renameSet(id,URLDecoder.decode(setListName,StandardCharsets.UTF_8.name()));
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
		}
	}
}
