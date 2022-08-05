/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import static com.sjwi.catalog.config.PreferencesConfiguration.NIGHT_MODE_PREFERENCE_KEY;
import static com.sjwi.catalog.model.security.StoredCookieToken.STORED_COOKIE_TOKEN_KEY;

import com.sjwi.catalog.exception.DatabaseException;
import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.Organization;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.api.setlist.NewSetList;
import com.sjwi.catalog.model.security.SecurityToken;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.AddressBookService;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.TokenService;
import com.sjwi.catalog.service.UserService;
import eu.bitwalker.useragentutils.UserAgent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.util.WebUtils;

@Component
public class ControllerHelper {

  @Autowired AddressBookService addressBookService;

  @Autowired @Lazy SetListService setListService;

  @Autowired TokenService tokenService;

  @Autowired UserService userService;

  @Autowired OrganizationService organizationService;

  @Autowired Mailer mailer;

  @Autowired CustomLogger logger;

  @Autowired ServletContext context;

  private static final Pattern IS_INT_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");
  private static final List<String> NO_CAPS_TITLE_WORDS =
      new ArrayList<String>(
          Arrays.asList("The", "Is", "A", "And", "But", "An", "At", "To", "For", "Of"));

  public String buildSetlistName(NewSetList setList) {
    String setListName = "Untitled";
    if (setList.getUnit() == 0) {
      setListName =
          setList.getOtherUnit() == null || "".equals(setList.getOtherUnit())
              ? "Untitled"
              : setList.getOtherUnit().trim();
    } else {
      setListName = organizationService.getOrganizationById(setList.getUnit()).getName().trim();
    }
    if (0 == setList.getSubUnit()) {
      setListName =
          setList.getOtherSubUnit() == null || "".equals(setList.getOtherSubUnit())
              ? setListName
              : setListName + " " + WordUtils.capitalize(setList.getOtherSubUnit());
    } else if (2 == setList.getSubUnit()) {
      if (setList.getGroupId() == 0) {
        String newHomegroupName = WordUtils.capitalize(setList.getOtherGroupName().trim());
        setListName = setListName + " " + newHomegroupName + " HF";
        organizationService.addGroup(newHomegroupName);
      } else {
        String homeGroupName = organizationService.getGroupById(setList.getGroupId());
        setListName = setListName + " " + homeGroupName + " HF";
      }
    } else {
      setListName =
          setListName
              + " "
              + organizationService.getMeetingServiceById(setList.getSubUnit()).trim();
    }
    if (setList.getDate() == null) {
      setList.setDate(new Date());
    }
    setListName = setListName + " " + new SimpleDateFormat("MM-dd-yyyy").format(setList.getDate());
    return setListName.replaceAll(" +", " ");
  }

  public void logPageRequest(String signature) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

    String requestUrl = request.getServletPath().toString();
    String ipAddress =
        request.getHeader("X-FORWARDED-FOR") == null
                || request.getHeader("X-FORWARDED-FOR").trim().isEmpty()
            ? request.getRemoteAddr()
            : request.getHeader("X-FORWARDED-FOR");
    String parameters =
        request.getParameterMap().entrySet().stream()
            .map(
                p ->
                    "["
                        + p.getKey()
                        + ": "
                        + String.join(",", request.getParameterMap().get(p.getKey()))
                        + "]")
            .collect(Collectors.joining(";"));
    String username = userService.getSessionUsername();
    String os = getOs();
    String protocol = request.getMethod();
    boolean standAloneMode = isStandAlone();

    logger.info(
        "'"
            + requestUrl
            + "' :: "
            + signature
            + "\n\t\t"
            + parameters
            + "\n\tcalled by "
            + username
            + " on a "
            + os
            + " device ("
            + ipAddress
            + ").\n\n");
    userService.logUserAction(
        username, os, ipAddress, signature, requestUrl, standAloneMode, protocol, parameters);
  }

  private boolean isStandAlone() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    Cookie standAloneModeCookie = WebUtils.getCookie(request, "STAND_ALONE");
    return standAloneModeCookie != null && Boolean.valueOf(standAloneModeCookie.getValue());
  }

  public String getOs() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    String agent = request.getHeader("User-Agent");
    return UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
  }

  public ModelAndView errorHandler(Exception e) {
    logger.logErrorWithEmail(
        "User "
            + userService.getSessionUsername()
            + " on a "
            + getOs()
            + " device.\n"
            + httpServletRequestToString()
            + "\n"
            + ExceptionUtils.getStackTrace(e));
    ModelAndView mv = new ModelAndView("error");
    mv.setStatus(HttpStatus.INTERNAL_SERVER_ERROR);
    try {
      mv.addObject("orgs", organizationService.getOrganizations());
    } catch (Exception exception) {
      mv.addObject("orgs", new ArrayList<Organization>());
    }
    return mv;
  }

  public String recipientsToString(List<String> recipients) {
    return recipients == null
        ? ""
        : recipients.stream()
            .map(
                t -> {
                  if (IS_INT_PATTERN.matcher(t).matches()) {
                    try {
                      return addressBookService
                          .getAddressBookEntryById(Integer.parseInt(t))
                          .getEmail();
                    } catch (NumberFormatException e) {
                      errorHandler(e);
                      return null;
                    }
                  } else {
                    if (t.contains("@")) {
                      return t;
                    } else {
                      return null;
                    }
                  }
                })
            .filter(Objects::nonNull)
            .collect(Collectors.joining(","));
  }

  public SetList buildSetFile(int id, FileGenerator fileGenerator, boolean lyricsOnly)
      throws DatabaseException, FileUtilityException {
    SetList setList =
        lyricsOnly ? setListService.getLyricsToSetListById(id) : setListService.getSetListById(id);
    fileGenerator.buildFile(setList);
    return setList;
  }

  public String normalizeString(String string) {
    return string == null ? null : string.replace("/", "_").replace("\\", "_").replace(";", ".");
  }

  public String buildHtmlLinkFromUrl(String link, String linkName) {
    return "<a href=\"" + link + "\">" + linkName + "</a>";
  }

  public String getFullUrl() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
    String queryString = request.getQueryString();

    if (queryString == null) {
      return requestURL.toString();
    } else {
      return requestURL.append('?').append(queryString).toString();
    }
  }

  public String httpServletRequestToString() {

    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    StringBuilder sb = new StringBuilder();

    sb.append("Request Method = [" + request.getMethod() + "], ");
    sb.append("Request URL Path = [" + request.getRequestURL() + "], ");

    String headers =
        Collections.list(request.getHeaderNames()).stream()
            .map(
                headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)))
            .collect(Collectors.joining(", "));

    if (headers.isEmpty()) {
      sb.append("Request headers: NONE,");
    } else {
      sb.append("Request headers: [" + headers + "],");
    }

    String parameters =
        Collections.list(request.getParameterNames()).stream()
            .map(p -> p + " : " + java.util.Arrays.asList(request.getParameterValues(p)))
            .collect(Collectors.joining(", "));

    if (parameters.isEmpty()) {
      sb.append("Request parameters: NONE.");
    } else {
      sb.append("Request parameters: [" + parameters + "].");
    }

    return sb.toString();
  }

  public void setCookiesInSession() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    if (request.getSession(false) == null) {
      request.getSession(true);
      logger.logSessionCreation(request);
    }
    if (request.getCookies() != null) {
      Arrays.stream(request.getCookies())
          .forEach(
              c -> {
                if (NIGHT_MODE_PREFERENCE_KEY.equals(c.getName())) {
                  request.getSession().setAttribute(c.getName(), c.getValue());
                }
              });
    }
  }

  public void attemptUserLoginViaCookie() {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    try {
      if (request.getSession(false) == null
          || (SecurityContextHolder.getContext().getAuthentication() != null
              && SecurityContextHolder.getContext().getAuthentication().getPrincipal()
                  instanceof UserDetails)) {
        return;
      }
      if (request.getCookies() != null) {
        Optional<Cookie> userCookie =
            Arrays.stream(request.getCookies())
                .filter(c -> STORED_COOKIE_TOKEN_KEY.equals(c.getName()))
                .findFirst();
        if (userCookie.isPresent()) {
          SecurityToken token = tokenService.getStoredCookieToken(userCookie.get().getValue());
          if (token != null && token.isTokenValid()) {
            CfUser user = (CfUser) userService.loadUserByUsername(token.getUser());
            SecurityContextHolder.getContext()
                .setAuthentication(
                    new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
            setSessionPreferences(user);
            logger.logSignInFromCookie(request, user.getUsername(), isStandAlone());
          }
        }
      }
    } catch (Exception e) {
      logger.logErrorWithEmail(e.getMessage());
    }
  }

  public void setSessionPreferences(CfUser cfUser) {
    HttpServletRequest request =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
    HttpServletResponse response =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
    if (request.getSession(false) == null) {
      request.getSession(true);
    }
    cfUser.getPreferences().entrySet().stream()
        .forEach(
            p -> {
              request.getSession().setAttribute(p.getKey(), p.getValue());
              if (NIGHT_MODE_PREFERENCE_KEY.equalsIgnoreCase(p.getKey())) {
                response.addCookie(buildStaticCookie(p.getKey(), p.getValue()));
              }
            });
  }

  public static Cookie buildStaticCookie(String key, String setting) {
    String host =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest()
            .getServerName();
    Cookie[] cookies =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest()
            .getCookies();
    Cookie cookie = null;
    if (cookies != null && Arrays.stream(cookies).anyMatch(c -> c.getName().equals(key))) {
      cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(key)).findFirst().orElse(null);
      cookie.setValue(setting);
    } else {
      cookie = new Cookie(key, setting);
    }
    cookie.setMaxAge(60 * 60 * 24 * 7 * 52 * 10);
    cookie.setDomain(host);
    return cookie;
  }

  public String titleCase(String title) {
    String titleCasedString = WordUtils.capitalize(title);
    for (String w : NO_CAPS_TITLE_WORDS)
      titleCasedString = titleCasedString.replace(" " + w + " ", " " + w.toLowerCase() + " ");
    return titleCasedString.substring(0, 1).toUpperCase() + titleCasedString.substring(1);
  }
}
