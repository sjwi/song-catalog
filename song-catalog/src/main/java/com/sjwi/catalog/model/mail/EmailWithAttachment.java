/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model.mail;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.URLDataSource;
import javax.mail.BodyPart;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class EmailWithAttachment extends Email {

  private Map<String, URL> attachments;

  @Override
  protected void setMessageContent() throws MessagingException {
    if (emailHasAttachments()) {
      Multipart multipart = new MimeMultipart();
      multipart.addBodyPart(getMessageBodyPart());
      for (Map.Entry<String, URL> att : attachments.entrySet()) {
        multipart.addBodyPart(getAttachmentBodyPart(att.getKey(), att.getValue()));
      }
      message.setContent(multipart);
    }
  }

  private boolean emailHasAttachments() {
    return attachments != null && attachments.size() != 0;
  }

  private BodyPart getMessageBodyPart() throws MessagingException {
    BodyPart messageBodyPart = new MimeBodyPart();
    messageBodyPart.setContent(body, CONTENT_TYPE);
    return messageBodyPart;
  }

  private BodyPart getAttachmentBodyPart(String fileName, URL fileUrl) throws MessagingException {
    BodyPart attachmentBodyPart = new MimeBodyPart();
    DataSource source = new URLDataSource(fileUrl);
    attachmentBodyPart.setDataHandler(new DataHandler(source));
    attachmentBodyPart.setFileName(fileName);
    return attachmentBodyPart;
  }

  public EmailWithAttachment setAttachments(Map<String, URL> attachments) {
    this.attachments = attachments;
    return this;
  }

  public EmailWithAttachment setAttachment(Map.Entry<String, URL> attachment) {
    Map<String, URL> map = new HashMap<>();
    map.put(attachment.getKey(), attachment.getValue());
    this.attachments = map;
    return this;
  }
}
