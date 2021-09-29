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
import com.sjwi.catalog.aspect.ServletInitializerAspect;
import com.sjwi.catalog.model.Log;
import com.sjwi.catalog.model.ResponseMessage;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.ModelAndView;

@Controller
@IgnoreAspect
public class ReportController {

	@Autowired
	UserService userService;

	@Autowired
	ControllerHelper controllerHelper;

	@RequestMapping(value = {"log"}, method = RequestMethod.GET)
	@LandingPageAspect
	public ModelAndView logPage(HttpServletResponse response, HttpServletRequest request, Authentication auth) throws IOException {
		try {
			if (request.getUserPrincipal() == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("SUPERADMIN")))
				response.sendRedirect("/login");
			return new ModelAndView("log");
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value = {"logstream"}, method = RequestMethod.GET)
	@LandingPageAspect
	public void streamUserFeed(HttpServletResponse response, HttpServletRequest request, Authentication auth) throws IOException {
		try {
			if (request.getUserPrincipal() == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("SUPERADMIN")))
				response.sendRedirect("/login");
			response.setContentType("text/plain; name=\"userfeed.txt\"");
			response.addHeader("Content-Disposition", "inline; filename=\"userfeed.txt\"");
			Files.copy(Paths.get(System.getProperty(LOG_FILE_PROPERTY_KEY)), response.getOutputStream());
		} catch (Exception e){
			controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value={"structured-logs"}, method= RequestMethod.GET)
	@LandingPageAspect
	public ModelAndView structuredLogs(HttpServletResponse response, HttpServletRequest request, Authentication auth){
		try {
			if (request.getUserPrincipal() == null || !auth.getAuthorities().stream().anyMatch(a -> a.getAuthority().equalsIgnoreCase("SUPERADMIN")))
				response.sendRedirect("/login");
			return new ModelAndView("structured-logs");
		} catch (Exception e){
			return controllerHelper.errorHandler(e);
		}
	}

	@RequestMapping(value={"structured-logs/json"}, method= RequestMethod.GET)
	@LandingPageAspect
	@ResponseBody
	public LogData structuredLogData(HttpServletResponse response, HttpServletRequest request, Authentication auth){
		List<Log> data = userService.getLogData();
		return new LogData(data);
	}

	public class LogData {
		public List<Log> data;
		public LogData(List<Log> logs){
			data = logs;
		}
	}

	@RequestMapping(value = {"/user-report"}, method = RequestMethod.GET)
	@ResponseBody
	public String displayUserReport(HttpServletRequest request) {
		List<CfUser> user = userService.getAllActiveUsers();
		return "Active users (" + user.size() + ")<br>" +  user.stream().map(u -> u.getUsername()).collect(Collectors.joining("<br>"));
	}

	@ServletInitializerAspect
	@RequestMapping(value = {"/server-availability"}, method = RequestMethod.GET)
	@ResponseStatus(HttpStatus.OK)
	@ResponseBody
	public ResponseMessage displayServerAvailability(HttpServletRequest request) {
		return new ResponseMessage("available","none");
	}
}
