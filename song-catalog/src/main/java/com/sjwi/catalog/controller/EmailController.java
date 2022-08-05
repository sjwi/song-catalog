/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.exception.MailException;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.mail.Email;
import com.sjwi.catalog.model.mail.EmailRequestBody;
import com.sjwi.catalog.model.user.CfUser;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class EmailController {

  @Autowired ControllerHelper controllerHelper;
  @Autowired Mailer mailer;

  @PostMapping("/email")
  public ResponseEntity<Object> sendAddressBookGroupEmail(
      Authentication auth,
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestBody @Valid EmailRequestBody emailRequest)
      throws MailException {
    String userEmail = ((CfUser) auth.getPrincipal()).getEmail();
    if (!emailRequest.getEmailTo().contains(userEmail)
        && !emailRequest.getEmailToCc().contains(userEmail))
      emailRequest.getEmailTo().add(userEmail);
    mailer.sendMail(
        new Email()
            .setTo(controllerHelper.recipientsToString(emailRequest.getEmailTo()))
            .setCc(controllerHelper.recipientsToString(emailRequest.getEmailToCc()))
            .setBody(emailRequest.getFinalMessage())
            .setSubject(emailRequest.getSubject()));
    return ResponseEntity.noContent().build();
  }
}
