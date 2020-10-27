package com.sjwi.cfsongs.mail;
import java.util.Arrays;
import java.util.stream.Collectors;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.exception.MailException;
import com.sjwi.cfsongs.log.CustomLogger;
import com.sjwi.cfsongs.model.mail.Email;

@Component
public class Mailer {
	
	@Value("${SONG_MAIL_LEVEL}")
	private String mailLevel;
	
	@Autowired
	Environment env;
	
	@Autowired
	Session session;
	
	@Autowired
	CustomLogger logger;
	
	public void sendMail(Email email) throws MailException {
		
		try {
			sendMessage(email.getEmailMessage(session));
		}
		catch (MessagingException e) {
			throw new MailException("Unable to send email\n" + email.toString(), e);
		}
		
	}

	private void sendMessage(Message message) throws MessagingException {
		logger.info("Attempting to send email to " + String.join(",",Arrays.stream(message.getRecipients(Message.RecipientType.TO)).map(r -> r.toString()).collect(Collectors.toList())));
		if ("LIVE".equals(mailLevel)) {
			Transport.send(message);
		}
		logger.info("Email successfully sent");
	}
}
