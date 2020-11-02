package com.sjwi.cfsongs.controller;

import static com.sjwi.cfsongs.log.CustomLogger.LOG_FILE_PROPERTY_KEY;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.sjwi.cfsongs.aspect.IgnoreAspect;
import com.sjwi.cfsongs.aspect.LandingPageAspect;
import com.sjwi.cfsongs.exception.MailException;
import com.sjwi.cfsongs.model.user.CfUser;
import com.sjwi.cfsongs.service.UserService;

@Controller
@IgnoreAspect
public class ReportController {
	@Autowired
	UserService userService;

	@RequestMapping(value = {"log"}, method = RequestMethod.GET)
	@LandingPageAspect
	public void streamUserFeed(HttpServletResponse response, HttpServletRequest request) throws IOException {
		if (request.getUserPrincipal() == null) {
			response.sendRedirect("/login");
		}
		response.setContentType("text/plain; name=\"userfeed.txt\"");
		response.addHeader("Content-Disposition", "inline; filename=\"userfeed.txt\"");
		Files.copy(Paths.get(System.getProperty(LOG_FILE_PROPERTY_KEY)), response.getOutputStream());
	}

	@RequestMapping(value = {"/user-report"}, method = RequestMethod.GET)
	@ResponseBody
	public String displayUserReport(HttpServletRequest request) throws MailException {
		List<CfUser> user = userService.getAllActiveUsers();
		return "Active users (" + user.size() + ")<br>" +  user.stream().map(u -> u.getUsername()).collect(Collectors.joining("<br>"));
	}
}
