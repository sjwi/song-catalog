package com.sjwi.catalog.controller;

import static com.sjwi.catalog.config.PreferencesConfiguration.NIGHT_MODE_PREFERENCE_KEY;
import static com.sjwi.catalog.model.security.StoredCookieToken.STORED_COOKIE_TOKEN_KEY;

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

import com.sjwi.catalog.exception.DatabaseException;
import com.sjwi.catalog.exception.FileUtilityException;
import com.sjwi.catalog.file.FileGenerator;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.Organization;
import com.sjwi.catalog.model.SetList;
import com.sjwi.catalog.model.security.SecurityToken;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.AddressBookService;
import com.sjwi.catalog.service.OrganizationService;
import com.sjwi.catalog.service.SetListService;
import com.sjwi.catalog.service.TokenService;
import com.sjwi.catalog.service.UserService;

import org.apache.commons.lang.WordUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.ModelAndView;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class ControllerHelper {

	@Autowired
	AddressBookService addressBookService;
	
	@Autowired
	SetListService setListService;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	OrganizationService organizationService;
	
	@Autowired
	Mailer mailer;
	
	@Autowired
	CustomLogger logger;

	@Autowired
	ServletContext context;

	private static final Pattern IS_INT_PATTERN = Pattern.compile("-?\\d+(\\.\\d+)?");

	public String buildSetlistName(int org, int service, String otherOrgName, String otherServiceName, Date date, String homegroupName) {
		String setListName = "Untitled";
		if (org == 0) {
			setListName = otherOrgName==null || "".equals(otherOrgName)? "Untitled" : otherOrgName;
		} else {
			setListName = organizationService.getOrganizationById(org).getName();
		}
		if (0 == service) {
			setListName = otherServiceName==null || "".equals(otherServiceName)? setListName : setListName + " " + WordUtils.capitalizeFully(otherServiceName);
		} else if (2 == service) {
			setListName = homegroupName == null || homegroupName.isEmpty()? 
					setListName + " " + organizationService.getMeetingServiceById(service): 
						setListName + " " + WordUtils.capitalizeFully(homegroupName) + " HF";
		} else {
			setListName = setListName + " " + organizationService.getMeetingServiceById(service);
		}
		if (date == null) {
			date = new Date();
		}
		setListName = setListName + " " + new SimpleDateFormat("MM-dd-yyyy").format(date);
		return setListName;
	}

	public String getOs() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		String agent = request.getHeader("User-Agent");
		return UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
	}
	
	public ModelAndView errorHandler(Exception e) {
        logger.logErrorWithEmail("User " + getSessionUsername() + " on a " + getOs() + " device.\n" + 
			httpServletRequestToString() + "\n" + 
			ExceptionUtils.getStackTrace(e));
		ModelAndView mv = new ModelAndView("error");
		try {
			mv.addObject("orgs",organizationService.getOrganizations());
		} catch (Exception exception){
			mv.addObject("orgs", new ArrayList<Organization>());
		}
		return mv;
	}

	public String getSessionUsername() {
		try {
			return ((CfUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUsername();
		} catch (Exception e) {
			return "anonymousUser";
		}
	}

	public String recipientsToString(List<String> recipients) {
		return recipients == null? "": recipients.stream().map(t -> {
			if (IS_INT_PATTERN.matcher(t).matches()) {
				try {
					return addressBookService.getAddressBookEntryById(Integer.parseInt(t)).getEmail();
				} catch (NumberFormatException  e) {
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
		}).filter(Objects::nonNull).collect(Collectors.joining(","));
	}
	
	public SetList buildSetFile(int id, FileGenerator fileGenerator, boolean lyricsOnly) throws DatabaseException, FileUtilityException {
		SetList setList = lyricsOnly? setListService.getLyricsToSetListById(id): setListService.getSetListById(id);
		fileGenerator.buildFile(setList);
		return setList;
	}
	public String normalizeString (String string) {
		return string == null? null: string.replace("/", "_").replace("\\", "_").replace(";", ".");
	}
	
	public String buildHtmlLinkFromUrl(String link, String linkName) {
		return "<a href=\"" + link + "\">" + linkName + "</a>";
	}

	public String getFullUrl() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
	    StringBuilder requestURL = new StringBuilder(request.getRequestURL().toString());
	    String queryString = request.getQueryString();

	    if (queryString == null) {
	        return requestURL.toString();
	    } else {
	        return requestURL.append('?').append(queryString).toString();
	    }
	}
	public String httpServletRequestToString() {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		StringBuilder sb = new StringBuilder();

		sb.append("Request Method = [" + request.getMethod() + "], ");
		sb.append("Request URL Path = [" + request.getRequestURL() + "], ");

		String headers = Collections.list(request.getHeaderNames()).stream()
				.map(headerName -> headerName + " : " + Collections.list(request.getHeaders(headerName)))
				.collect(Collectors.joining(", "));

		if (headers.isEmpty()) {
			sb.append("Request headers: NONE,");
		} else {
			sb.append("Request headers: [" + headers + "],");
		}

		String parameters = Collections.list(request.getParameterNames()).stream()
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
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		if (request.getSession(false) == null) {
			request.getSession(true);
			logger.logSessionCreation(request);
		}
		if (request.getCookies() != null) {
			Arrays.stream(request.getCookies()).forEach(c -> {
				if(NIGHT_MODE_PREFERENCE_KEY.equals(c.getName())) {
					request.getSession().setAttribute(c.getName(), c.getValue());
				}
			});
		}
	}
	
	public void attemptUserLoginViaCookie() {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		try {
			if (request.getSession(false) == null 
					|| (SecurityContextHolder.getContext().getAuthentication() != null
						&& SecurityContextHolder.getContext().getAuthentication().getPrincipal() instanceof UserDetails)) {
				return;
			}
			if (request.getCookies() != null) {
				Optional<Cookie> cookie = Arrays.stream(request.getCookies()).filter(c -> STORED_COOKIE_TOKEN_KEY.equals(c.getName())).findFirst();
				if (cookie.isPresent()) {
					SecurityToken token = tokenService.getStoredCookieToken(cookie.get().getValue());
					if (token != null && token.isTokenValid()) {
						CfUser user = (CfUser) userService.loadUserByUsername(token.getUser());
						SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
						setSessionPreferences(user);
						logger.logSignInFromCookie(request, user.getUsername());
					}
				}
			}
		} catch (Exception e ) {
			logger.logErrorWithEmail(e.getMessage());
		}
	}

	public void setSessionPreferences(CfUser cfUser) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
		HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getResponse();
		if (request.getSession(false) == null) {
			request.getSession(true);
		}
		cfUser.getPreferences().entrySet().stream().forEach(p -> {
			request.getSession().setAttribute(p.getKey(), p.getValue());
			if (NIGHT_MODE_PREFERENCE_KEY.equalsIgnoreCase(p.getKey())) {
				response.addCookie(buildStaticCookie(request.getServerName(),p.getKey(),p.getValue(),request.getCookies()));
			}
		});
	}

	public Cookie buildStaticCookie(String host, String key, String setting, Cookie[] cookies) {
		Cookie cookie = null;
		if (cookies != null && Arrays.stream(cookies).anyMatch(c -> c.getName().equals(key))) {
			cookie = Arrays.stream(cookies).filter(c -> c.getName().equals(key)).findFirst().orElse(null);
			cookie.setValue(setting);
		} else {
			cookie = new Cookie(key,setting);
		}
		cookie.setMaxAge(60 * 60 * 24 * 7 * 52 * 10);
		cookie.setDomain(host);
		return cookie;
	}

	public String buildTokenLink(HttpServletRequest request, SecurityToken token, String endpoint) {
		return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() 
				+ context.getContextPath() + "/" + endpoint 
				+ "?token=" + token.getTokenString() 
				+ "&user=" + token.getUser(); 
	}
	
	public String titleCase(String title) {
		String titleCasedString = WordUtils.capitalizeFully(title)
		.replace(" The ", " the ")
		.replace(" A ", " a ")
		.replace(" And ", " and ")
		.replace(" But ", " but ")
		.replace(" An ", " an ")
		.replace(" At ", " at ")
		.replace(" To ", " to ")
		.replace(" Is ", " is ")
		.replace(" For ", " for ")
		.replace(" Of ", " of ");
		return titleCasedString.substring(0,1).toUpperCase() + titleCasedString.substring(1);
	}
}
