package com.sjwi.catalog.controller.uac;

import java.security.Principal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.model.user.UserState;
import com.sjwi.catalog.service.UserService;

@Controller
public class UserStateController {

  @Autowired
  UserService userService;

  @Autowired
  ControllerHelper controllerHelper;

	@RequestMapping(value = {"/user-state"}, method = RequestMethod.GET)
	@ResponseBody
	public UserState getUserState(HttpServletRequest request, HttpServletResponse response,
			Principal principal) {
		try {
      return userService.getUserState(principal);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
      throw e;
		}
	}

	@RequestMapping(value = {"/user-state"}, method = RequestMethod.POST)
	@ResponseBody
	@ResponseStatus(HttpStatus.OK)
	public void setUserState(HttpServletRequest request, HttpServletResponse response,
      @RequestBody UserState userState,
			Principal principal) {
		try {
			if (userService.getUserState(principal).isDefault())
				userService.addUserState(userState, principal);
			else
				userService.setUserState(userState, principal);
		} catch (Exception e) {
			controllerHelper.errorHandler(e);
      throw e;
		}
	}
}
