/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.service;

import static com.sjwi.catalog.model.security.StoredCookieToken.ANONYMOUS_COOKIE_TOKEN_KEY;

import com.sjwi.catalog.controller.ControllerHelper;
import com.sjwi.catalog.dao.UserDao;
import com.sjwi.catalog.exception.PasswordException;
import com.sjwi.catalog.model.LogEntry;
import com.sjwi.catalog.model.SetListState;
import com.sjwi.catalog.model.SetListState.SetSongSetting;
import com.sjwi.catalog.model.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.model.user.UserState;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.thymeleaf.util.StringUtils;

@Service("userDetailsService")
public class UserService implements UserDetailsService {

  @Autowired AddressBookService addressBookService;

  @Autowired @Lazy private PasswordEncoder passwordEncoder;

  @Autowired UserDao userDao;

  private static Map<String, CfUser> cachedUsers = new HashMap<>();

  public CfUser createUser(
      String username,
      String firstName,
      String lastName,
      String email,
      String password,
      List<String> authorityNames,
      Map<String, String> preferences) {
    password = passwordEncoder.encode(password);
    List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
    authorityNames.stream()
        .forEach(
            a -> {
              authorities.add(new SimpleGrantedAuthority(a.toUpperCase()));
            });
    CfUser user =
        new CfUser(
            username.trim().toLowerCase(),
            firstName,
            lastName,
            email,
            password,
            authorities,
            preferences);
    updateAddressBookEntryForUser(user);
    userDao.saveUser(user);
    userDao.saveUserAuthorities(user.getUsername(), user.getAuthorities());
    return user;
  }

  private void updateAddressBookEntryForUser(CfUser user) {
    AddressBookEntry existingUser = addressBookService.getAddressBookEntryByEmail(user.getEmail());
    if (existingUser == null) {
      addressBookService.createEntry(
          new AddressBookEntry(
              0,
              user.getFirstName(),
              user.getLastName(),
              user.getUsername(),
              user.getEmail(),
              null));
    } else {
      addressBookService.editEntryByEmail(
          new AddressBookEntry(
              0,
              user.getFirstName(),
              user.getLastName(),
              user.getUsername(),
              user.getEmail(),
              null));
    }
  }

  public void changePassword(String username, String oldPassword, String newPassword)
      throws PasswordException {
    if (!passwordEncoder.matches(oldPassword, userDao.getPassword(username))) {
      throw new PasswordException("Bad password");
    }
    newPassword = passwordEncoder.encode(newPassword);
    userDao.changePassword(username.trim().toLowerCase(), newPassword);
  }

  public void resetPassword(String username, String newPassword) throws PasswordException {
    newPassword = passwordEncoder.encode(newPassword);
    userDao.changePassword(username.trim().toLowerCase(), newPassword);
  }

  @Override
  public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
    return userDao.getUser(username.trim().toLowerCase());
  }

  public synchronized CfUser loadCfUserByUsername(String username)
      throws UsernameNotFoundException {
    username = username.trim().toLowerCase();
    if (cachedUsers.containsKey(username)) {
      return cachedUsers.get(username);
    } else {
      CfUser user = (CfUser) userDao.getUser(username);
      cachedUsers.put(username, user);
      return user;
    }
  }

  public List<CfUser> getAllActiveUsers() {
    return userDao.getAllActiveUsers();
  }

  public List<String> getAllEnrollmentEmails() {
    return userDao.getAllEnrollmentEmails();
  }

  public boolean isUsernameTaken(String username) {
    return userDao.isUsernameTaken(username);
  }

  public void logUserAction(
      String username,
      String os,
      String ipAddress,
      String signature,
      String requestUrl,
      boolean standAloneMode,
      String protocol,
      String parameters) {
    userDao.log(
        username, os, ipAddress, signature, requestUrl, standAloneMode, protocol, parameters);
  }

  public List<LogEntry> getLogData() {
    return userDao.getLogData();
  }

  public boolean userHasRequestedAccount(String email) {
    return userDao.getAccountRequestDetails(email).size() != 0;
  }

  public void storeAccountRequest(String email) {
    userDao.storeAccountRequest(email);
  }

  public void createAnonymousUser(String token) {
    userDao.createAnonymousUser(token);
  }

  public String getAnonymousUser(String tokenLink) {
    return userDao.getAnonymousUser(tokenLink);
  }

  public String getSessionUsername() {
    try {
      return ((CfUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
          .getUsername();
    } catch (Exception e) {
      HttpServletRequest request =
          ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
      try {
        // session empty
        if (request.getSession().getAttribute(ANONYMOUS_COOKIE_TOKEN_KEY) == null) {
          // but cookie exists
          if (request.getCookies() != null
              && Arrays.stream(request.getCookies())
                  .filter(c -> ANONYMOUS_COOKIE_TOKEN_KEY.equals(c.getName()))
                  .findFirst()
                  .isPresent()) {
            request
                .getSession()
                .setAttribute(
                    ANONYMOUS_COOKIE_TOKEN_KEY,
                    getAnonymousUser(
                        Arrays.stream(request.getCookies())
                            .filter(c -> ANONYMOUS_COOKIE_TOKEN_KEY.equals(c.getName()))
                            .findFirst()
                            .get()
                            .getValue()));
          } else {
            // New user
            String cookieToken = StringUtils.randomAlphanumeric(200);
            createAnonymousUser(cookieToken);
            ControllerHelper.buildStaticCookie(ANONYMOUS_COOKIE_TOKEN_KEY, cookieToken);
            request
                .getSession()
                .setAttribute(ANONYMOUS_COOKIE_TOKEN_KEY, getAnonymousUser(cookieToken));
          }
        }
      } catch (Exception ex) {
        return "anonymousUser";
      }
      return request.getSession().getAttribute(ANONYMOUS_COOKIE_TOKEN_KEY).toString();
    }
  }

  public UserState getUserState(Principal principal) {
    return userDao.getUserState(principal.getName());
  }

  public void setUserState(UserState userState, Principal principal) {
    userDao.setUserState(userState, principal.getName());
  }

  public void addUserState(UserState userState, Principal principal) {
    userDao.addUserState(userState, principal.getName());
  }

  public SetListState getSetState(int setId) {
    String user = this.getSessionUsername();
    return userDao.getSetlistStateForUser(user, setId, false);
  }

  public void updateSetState(int setId, int setSongId, SetSongSetting state) {
    String user = this.getSessionUsername();
    SetListState existingSetState = userDao.getSetlistStateForUser(user, setId, true);
    existingSetState.updateSetting(setSongId, state);
    userDao.updateSetListSessionState(user, setId, existingSetState);
  }

  public void removeSetState(int setId, int songId) {
    String user = this.getSessionUsername();
    SetListState existingState = userDao.getSetlistStateForUser(user, setId, false);
    if (existingState.getSongSettings().containsKey(songId))
      existingState.getSongSettings().remove(songId);
    userDao.updateSetListSessionState(user, setId, existingState);
  }

  public Map<Integer, SetListState> getAllSetlistStatesForUser() {
    String user = this.getSessionUsername();
    return userDao.getAllSetlistStatesForUser(user);
  }
}
