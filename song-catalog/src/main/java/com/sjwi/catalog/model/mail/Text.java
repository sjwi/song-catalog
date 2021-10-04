package com.sjwi.catalog.model.mail;

import javax.annotation.PostConstruct;

import com.sjwi.catalog.log.CustomLogger;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

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

  @Autowired
  CustomLogger log;

  @PostConstruct
  public void initialize(){
    try {
      Twilio.init(twilioAccountSid, twilioAuthCode);
    } catch (Exception e) {
      log.error("Unable to initiate Twilio",e);
    }
  }

  public void sendText(String number, String msg) throws IllegalArgumentException {
    String formattedToNumber = stripPhoneNumber(number);
    Message.creator(new PhoneNumber(formattedToNumber),new PhoneNumber(twilioPhoneNumber), msg).create();
  }

  private String stripPhoneNumber(String number) {
    String rawNumber = number.replaceAll("\\D", "");
    if (rawNumber.length() == 10)
      return "+1" + rawNumber;
    else if (rawNumber.length() == 11 && rawNumber.substring(0,1).equals("1"))
      return "+" + rawNumber;
    else if (rawNumber.length() == 12 && rawNumber.substring(0,1).equals("+"))
      return rawNumber;
    throw new IllegalArgumentException(number + " is not a valid US phone number number");
  }
}
