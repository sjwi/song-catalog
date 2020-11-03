package com.sjwi.catalog.mail;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class MailAuthenticator extends Authenticator {

	@Value("${com.sjwi.settings.mail_un}")
	String username;

	@Value("${com.sjwi.settings.mail_pw}")
	String password;

	@Override
	protected PasswordAuthentication getPasswordAuthentication() {
		return new PasswordAuthentication(username, password);
	}
}
