package com.sjwi.catalog.log;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.sjwi.catalog.exception.MailException;
import com.sjwi.catalog.mail.MailConstants;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.mail.Email;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class CustomLogger {
	
	public static final String LOG_FILE_PROPERTY_KEY = "log.feed";

	@Autowired
	Mailer mailer;

	@Value("${com.sjwi.settings.app.properName}")
	String properName;

	private Logger log;

	@PostConstruct
	public void initialize() {
		System.setProperty(LOG_FILE_PROPERTY_KEY,System.getenv("CATALINA_BASE") + "/logs/songCatalogUserFeed");
		log = Logger.getLogger(CustomLogger.class.getName());
	}
	public void info(Object message) {
		log.info(message);
	}
	
	public void error(Object message) {
		log.error(message);
	}
	
	public void logMessageWithEmail(String message) {
		log.info(message);
		new Thread(new SendLogMessageWithEmail(message)).start();
	}

	public void logUserActionWithEmail(String message) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null) {
			message = message + " by " + auth.getName();
		} else {
			message = message + " by anonymous user";
		}
		log.info(message);
		new Thread(new SendLogMessageWithEmail(message)).start();
	}

	public void logErrorWithEmail(String message) {
		log.error(message);
		new Thread(new SendLogMessageWithEmail(message)).start();
	}
	
	public void logSignIn(HttpServletRequest request, String user) {
		String msg = user + " logged in on device " + getDeviceType(request);
		log.info(msg);
		new Thread(new SendLogMessageWithEmail(msg)).start();
	}
	public void logSessionCreation(HttpServletRequest request) {
		String msg =  "New session created on device " + getDeviceType(request);
		log.info(msg);
	}
	public void logSignInFromCookie(HttpServletRequest request, String user) {
		String msg = user + " logged in on device " + getDeviceType(request) + " by a stored token";
		log.info(msg);
		new Thread(new SendLogMessageWithEmail(msg)).start();
	}
	
	private String getDeviceType(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");	
		return UserAgent.parseUserAgentString(agent).getOperatingSystem().getDeviceType().toString() + " || "
				+ UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
	}
	
	private class SendLogMessageWithEmail implements Runnable {
		
		private String message;
		
		SendLogMessageWithEmail(String message){
			this.message = message;
		}
		@Override
		public void run() {
			try {
				mailer.sendMail(new Email()
						.setBody(message)
						.setTo(MailConstants.ADMIN_DISTRIBUTION_LIST)
						.setSubject(properName + " Log Notification"));
			} catch (MailException e) {
				e.printStackTrace();
				error(e);
			}
		}
		
	}
}
