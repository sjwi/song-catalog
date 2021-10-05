package com.sjwi.catalog.log;

import java.util.stream.Collectors;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;

import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.exception.MailException;
import com.sjwi.catalog.mail.MailConstants;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.mail.Email;
import com.sjwi.catalog.model.song.Song;
import com.sjwi.catalog.service.SongService;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import eu.bitwalker.useragentutils.UserAgent;

@Component
public class CustomLogger {
	
	public static final String LOG_FILE_PROPERTY_KEY = "log.feed";

	@Autowired
	Mailer mailer;

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

  public void error(String string, Exception e) {
		log.error(string,e);
  }
	
	public void logMessageWithEmail(String message) {
		log.info(message);
		new Thread(new SendLogMessageWithEmail(message)).start();
	}

	public void logUserActionWithEmail(String message) {
		message += " by " + getLoggedInUser();
		log.info(message);
		new Thread(new SendLogMessageWithEmail(message)).start();
	}

	public void logErrorWithEmail(String message) {
		log.error(message);
		new Thread(new SendLogMessageWithEmail(message, MailConstants.ERROR_SUBJECT)).start();
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
	public void mailRevisionDeltas(Song originalSong, Song revisedSong, String action){
		String deltaSummary = SongService.generateSongRevisionDiff(originalSong, revisedSong).stream()
								.map(d -> d.toString())
								.collect(Collectors.joining("\n\n - "));
		String message = action + " by " + getLoggedInUser() + ": " + revisedSong.getNormalizedName() + " (ID: " + revisedSong.getId() + ") \n\n";
		message += "Deltas:\n - " + deltaSummary + "\n\n";
		message += ServletConstants.FULL_URL + "/song/" + revisedSong.getId();
		logMessageWithEmail(message);
	}
	
	private String getDeviceType(HttpServletRequest request) {
		String agent = request.getHeader("User-Agent");	
		return UserAgent.parseUserAgentString(agent).getOperatingSystem().getDeviceType().toString() + " || "
				+ UserAgent.parseUserAgentString(agent).getOperatingSystem().toString();
	}

	private String getLoggedInUser(){
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if (auth != null)
			return auth.getName();
		return "anonymous user";
	}
	
	private class SendLogMessageWithEmail implements Runnable {
		
		private final String message;
		private final String subject;
		
		SendLogMessageWithEmail(String message){
			this.message = message;
			this.subject = MailConstants.LOG_NOTIFICATION_SUBJECT;
		}
		SendLogMessageWithEmail(String message, String subject){
			this.message = message;
			this.subject = subject;
		}
		@Override
		public void run() {
			try {
				mailer.sendPlainMail(new Email()
						.setBody(message)
						.setTo(MailConstants.ADMIN_DISTRIBUTION_LIST)
						.setSubject(subject));
			} catch (MailException e) {
				error(e);
			}
		}
		
	}
}
