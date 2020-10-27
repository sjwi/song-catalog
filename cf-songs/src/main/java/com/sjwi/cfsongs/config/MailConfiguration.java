package com.sjwi.cfsongs.config;

import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.mail.Session;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.sjwi.cfsongs.mail.MailAuthenticator;
import com.sjwi.cfsongs.mail.MailConstants;

@Configuration
public class MailConfiguration {
	
	@Autowired
	MailAuthenticator mailAuthenticator;

	@Value("${com.sjwi.settings.smtp}")
	String smtp;

	@Value("${com.sjwi.settings.smtpPort}")
	String smtpPort;

	@Value("${com.sjwi.settings.adminDistributionList}")
	private String adminDistributionList;

	@Value("${com.sjwi.settings.mail_un}")
	private String defaultFromAddress;

	@Bean
	public Properties properties() {
        Properties config = new Properties();
        config.put("mail.smtp.auth", "true");
        config.put("mail.smtp.starttls.enable", "true");
        config.put("mail.smtp.host", smtp);
        config.put("mail.smtp.port", smtpPort);
        return config;
	}
	
	@Bean
	public Session session() {
		return Session.getInstance(properties(),mailAuthenticator);
	}

	@PostConstruct
	public void initializeEmailConstants(){
		MailConstants.initializeMailConstants(defaultFromAddress, adminDistributionList);
	}
}
