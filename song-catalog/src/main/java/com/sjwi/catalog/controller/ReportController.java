package com.sjwi.catalog.controller;

import static com.sjwi.catalog.log.CustomLogger.LOG_FILE_PROPERTY_KEY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.sjwi.catalog.aspect.IgnoreAspect;
import com.sjwi.catalog.aspect.LandingPageAspect;
import com.sjwi.catalog.model.ResponseMessage;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@IgnoreAspect
public class ReportController {
	@Autowired
	UserService userService;

	@RequestMapping(value = {"log"}, method = RequestMethod.GET)
	@LandingPageAspect
	public void streamUserFeed(HttpServletResponse response, HttpServletRequest request) throws IOException {
		if (request.getUserPrincipal() == null)
			response.sendRedirect("/login");
		response.setContentType("text/plain; name=\"userfeed.txt\"");
		response.addHeader("Content-Disposition", "inline; filename=\"userfeed.txt\"");
		Files.copy(Paths.get(System.getProperty(LOG_FILE_PROPERTY_KEY)), response.getOutputStream());
	}

	@RequestMapping(value = {"/user-report"}, method = RequestMethod.GET)
	@ResponseBody
	public String displayUserReport(HttpServletRequest request) {
		List<CfUser> user = userService.getAllActiveUsers();
		return "Active users (" + user.size() + ")<br>" +  user.stream().map(u -> u.getUsername()).collect(Collectors.joining("<br>"));
	}

	@RequestMapping(value = {"/server-availability"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage displayServerAvailability(HttpServletRequest request) {
		return new ResponseMessage("available","none");
	}
}
