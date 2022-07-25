/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.mail;

import com.sjwi.catalog.mail.MailConstants;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Email {

  protected static final String CONTENT_TYPE = "text/html; charset=UTF-8";
  protected static final String PLAIN_CONTENT_TYPE = "text/plain; charset=UTF-8";

  private String to;
  private final String from = MailConstants.DEFAULT_FROM_ADDRESS;
  private String cc;
  protected String body;
  private String subject;

  protected Message message;

  public Message getEmailMessage(Session session) throws AddressException, MessagingException {
    message = new MimeMessage(session);
    setStaticMessageAttributes();
    setMessageContent();
    return message;
  }

  public Message getPlainEmailMessage(Session session) throws AddressException, MessagingException {
    message = new MimeMessage(session);
    setStaticMessageAttributes();
    message.setContent(body, PLAIN_CONTENT_TYPE);
    return message;
  }

  private void setStaticMessageAttributes() throws AddressException, MessagingException {
    message.addRecipients(Message.RecipientType.TO, InternetAddress.parse(to == null ? "" : to));
    message.addRecipients(Message.RecipientType.CC, InternetAddress.parse(cc == null ? "" : cc));
    message.setFrom(new InternetAddress(from));
    message.setSubject(subject);
  }

  protected void setMessageContent() throws MessagingException {
    message.setContent(body, CONTENT_TYPE);
  }

  @Override
  public String toString() {
    return "Email [to=" + to + ", from=" + from + ", cc=" + cc + ", subject=" + subject + "]";
  }

  public String getTo() {
    return to;
  }

  public String getCc() {
    return cc;
  }

  public String getBody() {
    return body;
  }

  public String getSubject() {
    return subject;
  }

  public Email setTo(String to) {
    this.to = to;
    return this;
  }

  public Email setCc(String cc) {
    this.cc = cc;
    return this;
  }

  public Email setBody(String body) {
    this.body = body;
    return this;
  }

  public Email setSubject(String subject) {
    this.subject = subject;
    return this;
  }
}
