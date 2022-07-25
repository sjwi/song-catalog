/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller.uac;

import static com.sjwi.catalog.model.security.StoredCookieToken.STORED_COOKIE_TOKEN_KEY;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.ResponseMessage;
import com.sjwi.catalog.model.security.SecurityToken;
import com.sjwi.catalog.model.security.StoredCookieToken;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.TokenService;
import com.sjwi.catalog.service.UserService;
import java.security.Principal;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class LoginController {

  @Autowired CustomLogger logger;

  @Autowired UserService userService;

  @Autowired ControllerHelper controllerHelper;

  @Autowired TokenService tokenService;

  @RequestMapping(
      value = {"/login"},
      method = RequestMethod.GET)
  public ModelAndView login(
      HttpServletRequest request, Authentication auth, HttpServletResponse response) {
    try {
      ModelAndView mv = new ModelAndView("forward:/home");
      mv.addObject("LOGIN", true);
      return mv;
    } catch (Exception e) {
      return controllerHelper.errorHandler(e);
    }
  }

  @RequestMapping(
      value = {"/login"},
      method = RequestMethod.POST)
  @ResponseBody
  public ResponseMessage login(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestParam(name = "username", required = true) String username,
      Principal principal,
      Authentication auth,
      @RequestParam(name = "password", required = true) String password) {
    try {
      request.login(username, password);
      logger.logSignIn(request, username);
      controllerHelper.setSessionPreferences(
          (CfUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal());
      generateCookieToken(request, response, username);
      return new ResponseMessage("success");
    } catch (ServletException e) {
      return new ResponseMessage("bad_password");
    } catch (Exception e) {
      return new ResponseMessage("bad_login_attempt");
    }
  }

  private void generateCookieToken(
      HttpServletRequest request, HttpServletResponse response, String user) {
    try {
      SecurityToken cookieToken = new StoredCookieToken(user);
      tokenService.storeCookieToken(cookieToken);
      response.addCookie(
          ControllerHelper.buildStaticCookie(
              STORED_COOKIE_TOKEN_KEY, cookieToken.getTokenString()));
    } catch (Exception e) {
      controllerHelper.errorHandler(e);
    }
  }
}
