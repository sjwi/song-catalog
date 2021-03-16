package com.sjwi.catalog.controller;

import static com.sjwi.catalog.config.PreferencesConfiguration.NIGHT_MODE_PREFERENCE_KEY;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.service.PreferencesService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PreferencesController {
	
	@Autowired
	CustomLogger logger;
	
	@Autowired
	PreferencesService preferencesService;
	
	@Autowired
	ControllerHelper controllerHelper;
	
	@RequestMapping(value = {"/toggle-night-mode"})
	public void toggleNightMode(HttpServletRequest request,
			HttpServletResponse response, Authentication auth, @RequestParam (name="setting", required=true) String setting) {
		request.getSession().setAttribute(NIGHT_MODE_PREFERENCE_KEY, setting);
		response.addCookie(controllerHelper.buildStaticCookie(request.getServerName(),NIGHT_MODE_PREFERENCE_KEY,setting,request.getCookies()));
		if (auth != null) 
			preferencesService.setUserPreference(NIGHT_MODE_PREFERENCE_KEY,setting, auth.getName());
	}

	@RequestMapping(value = {"/dark"})
	public void darkMode(HttpServletRequest request,
			HttpServletResponse response, Authentication auth) throws IOException {
		request.getSession().setAttribute(NIGHT_MODE_PREFERENCE_KEY, "true");
		response.addCookie(controllerHelper.buildStaticCookie(request.getServerName(),NIGHT_MODE_PREFERENCE_KEY,"true",request.getCookies()));
		response.sendRedirect("home");
	}
}
