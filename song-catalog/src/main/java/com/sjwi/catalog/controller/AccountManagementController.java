/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import java.nio.file.AccessDeniedException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.spec.InvalidKeySpecException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import com.sjwi.catalog.exception.MailException;
import com.sjwi.catalog.exception.PasswordException;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.mail.MailConstants;
import com.sjwi.catalog.mail.Mailer;
import com.sjwi.catalog.model.api.uac.ChangePasswordRequest;
import com.sjwi.catalog.model.api.uac.EnrollmentRequest;
import com.sjwi.catalog.model.api.uac.RequestAccountRequest;
import com.sjwi.catalog.model.api.uac.ResetPasswordRequest;
import com.sjwi.catalog.model.api.uac.SendInviteRequest;
import com.sjwi.catalog.model.mail.EmailFromTemplate;
import com.sjwi.catalog.model.security.EnrollmentToken;
import com.sjwi.catalog.model.security.PasswordResetToken;
import com.sjwi.catalog.model.security.SecurityToken;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.model.user.UserState;
import com.sjwi.catalog.service.TokenService;
import com.sjwi.catalog.service.UserService;

@RestController
public class AccountManagementController {

  private static final String INVITE_MSG_TEMPLATE = "email-templates/invitation.html";
  private static final String RESET_PASSWORD_MSG_TEMPLATE = "email-templates/password-reset.html";

  @Autowired ControllerHelper controllerHelper;

  @Autowired CustomLogger logger;

  @Autowired ServletContext context;

  @Autowired UserService userService;

  @Autowired TokenService tokenService;

  @Autowired Mailer mailer;

  @PostMapping("/change-password")
  public ResponseEntity<Object> changePassword(
      @RequestBody ChangePasswordRequest body, Principal principal) {
    if (!body.isValid()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    try {
      userService.changePassword(principal.getName(), body.getPassword(), body.getNewPassword());
    } catch (PasswordException e) {
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
    }
    return ResponseEntity.ok().build();
  }

  @PostMapping("/send-invite")
  public ResponseEntity<Object> sendInvite(
      @RequestBody SendInviteRequest body, Authentication auth, HttpServletRequest request)
      throws MailException, InvalidKeySpecException, NoSuchAlgorithmException {
    if (userService.getAllActiveUsers().stream()
        .anyMatch(u -> u.getEmail().equalsIgnoreCase(body.getInvitee())))
      return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
    if (userService.getAllEnrollmentEmails().stream()
        .anyMatch(u -> u.equalsIgnoreCase(body.getInvitee())))
      return new ResponseEntity<>("User already invited", HttpStatus.PRECONDITION_FAILED);
    EnrollmentToken token = new EnrollmentToken(body.getInvitee().toLowerCase(), body.getRole());
    tokenService.storeEnrollmentToken(token);
    mailer.sendMail(
        new EmailFromTemplate()
            .setMsgArgs(
                new String[] {
                  MailConstants.INVITATION_SUBJECT.replace(
                      "{{USERNAME}}", ((CfUser) auth.getPrincipal()).getFullName()),
                  token.getTokenLink()
                })
            .setMsgTemplate(INVITE_MSG_TEMPLATE)
            .setTo(body.getInvitee())
            .setSubject("You're Invited!"));
    logger.logMessageWithEmail(
        "Invitation sent to " + body.getInvitee() + " from " + auth.getName());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/password-reset")
  public ResponseEntity<Object> sendResetPasswordEmail(
      @RequestParam(name = "email", required = true) String email, HttpServletRequest request)
      throws MailException, InvalidKeySpecException, NoSuchAlgorithmException {
    CfUser user = (CfUser) userService.loadUserByUsername(email);
    if (user == null) return new ResponseEntity<>("Invalid user", HttpStatus.NOT_FOUND);
    SecurityToken token = new PasswordResetToken(user.getUsername());
    tokenService.storePasswordResetToken(token);
    mailer.sendMail(
        new EmailFromTemplate()
            .setMsgArgs(new String[] {user.getUsername(), token.getTokenLink()})
            .setMsgTemplate(RESET_PASSWORD_MSG_TEMPLATE)
            .setTo(user.getEmail())
            .setSubject(MailConstants.PASSWORD_RESET_SUBJECT));
    logger.logMessageWithEmail("Password reset link sent to " + user.getUsername());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/request-account")
  public ResponseEntity<Object> requestAccount(@RequestBody RequestAccountRequest body) {
    String email = body.getEmail().toLowerCase();
    if (userService.userHasRequestedAccount(email))
      return new ResponseEntity<>("User already request account", HttpStatus.PRECONDITION_FAILED);
    if (userService.loadUserByUsername(email) != null)
      return new ResponseEntity<>("User already exists", HttpStatus.CONFLICT);
    userService.storeAccountRequest(email);
    logger.logMessageWithEmail("Account requested for " + email);
    return ResponseEntity.ok().build();
  }

  @PostMapping("/reset-password")
  public ResponseEntity<Object> validatePasswordReset(
      @RequestBody ResetPasswordRequest body,
      Authentication auth,
      HttpServletRequest request,
      HttpServletResponse response)
      throws PasswordException, ServletException, AccessDeniedException {
    CfUser cfUser = (CfUser) userService.loadUserByUsername(body.getUser());
    SecurityToken securityToken =
        tokenService.getPasswordResetToken(cfUser.getUsername()).setSessionToken(body.getToken());
    if (!securityToken.isTokenValid()) throw new AccessDeniedException("Invalid security token");
    userService.resetPassword(cfUser.getUsername(), body.getPassword());
    request.login(cfUser.getUsername(), body.getPassword());
    logger.logSignIn(request, cfUser.getUsername());
    controllerHelper.setSessionPreferences(cfUser);
    tokenService.deletePasswordResetToken(cfUser.getUsername());
    return ResponseEntity.ok().build();
  }

  @PostMapping("/enroll")
  public ResponseEntity<CfUser> enroll(
      HttpServletRequest request,
      HttpServletResponse response,
      @RequestBody EnrollmentRequest body,
      Authentication auth)
      throws AccessDeniedException, ServletException {
    body.setUsername(body.getUsername().toLowerCase());
    EnrollmentToken token =
        tokenService.getEnrollmentToken(body.getUser()).setSessionToken(body.getToken());
    if (!token.isTokenValid()) throw new AccessDeniedException("Invalid enrollment token");
    List<String> authorities = new ArrayList<String>(Arrays.asList("USER"));
    if (("ADMIN").equals(token.getRole())) authorities.add("ADMIN");
    CfUser user = userService.createUser(body, authorities, null);
    request.login(user.getUsername(), body.getPassword());
    tokenService.deleteEnrollmentToken(token.getId());
    logger.logMessageWithEmail("New user enrolled: " + body.getUsername());
    logger.logSignIn(request, body.getUsername());
    return ResponseEntity.ok(user);
  }

  @GetMapping("/distinct-username")
  public ResponseEntity<Object> checkUsername(
      @RequestParam(name = "username", required = true) String username) {
    if (userService.isUsernameTaken(username))
      return new ResponseEntity<>("Already exists", HttpStatus.CONFLICT);
    if (username.contains(" "))
      return new ResponseEntity<>("Invalid username", HttpStatus.BAD_REQUEST);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/user/state")
  public ResponseEntity<UserState> getUserState(
      HttpServletRequest request, HttpServletResponse response, Principal principal) {
    UserState userState = userService.getUserState(principal);
    return userState == null ? ResponseEntity.ok(new UserState()) : ResponseEntity.ok(userState);
  }

  @PostMapping("/user/state")
  public ResponseEntity<Object> setUserState(
      @RequestBody UserState userState, Principal principal) {
    if (userService.getUserState(principal) == null) userService.addUserState(userState, principal);
    else userService.setUserState(userState, principal);
    return ResponseEntity.ok().build();
  }
}
