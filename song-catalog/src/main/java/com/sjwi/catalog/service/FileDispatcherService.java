/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import com.sjwi.catalog.config.ServletConstants;
import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.mail.EmailWithAttachment;
import com.sjwi.catalog.model.mail.Text;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.AbstractMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Component
public class FileDispatcherService {

  private static final String LOCAL_FILE_SUB_DIRECTORY = "downloads";
  private static final String MAIL_ATTACHMENT_BODY =
      "The attached file was sent from <a href=\"%s\">%s</a> <br><br>You can download the file directly with this link: %s";
  private static final String SMS_ATTACHMENT_BODY =
      "The attached file was sent from %s\n\nYou can download the file directly with this link: %s";
  private static final String SMS_LINK_BODY = "The linked file was sent from %s\n\n%s";

  @Autowired Text text;

  @Autowired Mailer mailer;

  @Autowired ControllerHelper controllerHelper;

  public Map.Entry<String, URL> getFileAsPathFromRestAPI(String apiEndpoint) throws IOException {
    HttpEntity<byte[]> response =
        new RestTemplate()
            .exchange(
                apiEndpoint, HttpMethod.GET, new HttpEntity<>(new HttpHeaders()), byte[].class);
    HttpHeaders headers = response.getHeaders();
    String fileName = headers.getContentDisposition().getFilename();
    String root =
        ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
            .getRequest()
            .getServletContext()
            .getRealPath("/");
    String localFileDir = root + "/" + LOCAL_FILE_SUB_DIRECTORY;
    new File(localFileDir).mkdir();
    String localFileName =
        localFileDir
            + "/downloaded_content_"
            + System.currentTimeMillis()
            + headers.getContentDisposition().getFilename();
    Path path = Paths.get(localFileName);
    Files.write(path, response.getBody());
    return new AbstractMap.SimpleEntry<String, URL>(
        localFileName, new URL(URLDecoder.decode(fileName, "UTF-8")));
  }

  @Async
  public void emailFileToRecipients(
      List<String> emailTo, Entry<String, URL> fileAttachment, String fileUrl) {
    emailTo.stream()
        .map(
            e ->
                new EmailWithAttachment()
                    .setAttachment(fileAttachment)
                    .setTo(e)
                    .setBody(
                        String.format(
                            MAIL_ATTACHMENT_BODY,
                            ServletConstants.SERVER_NAME,
                            ServletConstants.SERVER_NAME,
                            controllerHelper.buildHtmlLinkFromUrl(fileUrl, fileUrl)))
                    .setSubject("CF Song Catalog"))
        .forEach(
            e -> {
              try {
                mailer.sendMail(e);
              } catch (Exception ex) {
                ex.printStackTrace();
              }
            });
  }

  @Async
  public void smsFileToRecipients(
      List<String> textTo, Entry<String, URL> fileAttachment, String fileUrl)
      throws InterruptedException, IllegalArgumentException, UnsupportedEncodingException {
    for (int i = 0; i < textTo.size(); i++) {
      String number = normalizePhoneNumber(textTo.get(i));
      if (i != 0) TimeUnit.SECONDS.sleep(1);
      if (isFilePpt(fileAttachment.getKey()))
        text.sendText(number, String.format(SMS_LINK_BODY, ServletConstants.SERVER_NAME, fileUrl));
      else
        text.sendText(
            number,
            String.format(SMS_ATTACHMENT_BODY, ServletConstants.SERVER_NAME, fileUrl),
            URLDecoder.decode(fileUrl, "UTF-8").replaceAll(" ", ""));
    }
  }

  private boolean isFilePpt(String filename) {
    return filename.substring(filename.length() - 4).equalsIgnoreCase("pptx")
        || filename.substring(filename.length() - 3).equalsIgnoreCase("ppt");
  }

  private String normalizePhoneNumber(String number) {
    String rawNumber = number.replaceAll("\\D", "");
    if (rawNumber.length() == 10) return "+1" + rawNumber;
    else if (rawNumber.length() == 11 && rawNumber.substring(0, 1).equals("1"))
      return "+" + rawNumber;
    else if (rawNumber.length() == 12 && rawNumber.substring(0, 1).equals("+")) return rawNumber;
    throw new IllegalArgumentException(number + " is not a valid US phone number number");
  }
}
