/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.mail;

import com.sjwi.catalog.log.CustomLogger;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import java.net.URI;
import java.util.Arrays;
import javax.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class Text {

  @Value("${com.sjwi.settings.twilioAccount}")
  String twilioAccountSid;

  @Value("${com.sjwi.settings.twilioAuthCode}")
  String twilioAuthCode;

  @Value("${com.sjwi.settings.twilioPhoneNumber}")
  String twilioPhoneNumber;

  @Autowired CustomLogger log;

  @PostConstruct
  public void initialize() {
    try {
      Twilio.init(twilioAccountSid, twilioAuthCode);
    } catch (Exception e) {
      log.error("Unable to initiate Twilio", e);
    }
  }

  public void sendText(String number, String msg) throws IllegalArgumentException {
    Message.creator(new PhoneNumber(number), new PhoneNumber(twilioPhoneNumber), msg).create();
  }

  public void sendText(String number, String msg, String mediaUrl) throws IllegalArgumentException {
    Message.creator(new PhoneNumber(number), new PhoneNumber(twilioPhoneNumber), msg)
        .setMediaUrl(Arrays.asList(URI.create(mediaUrl)))
        .create();
  }
}
