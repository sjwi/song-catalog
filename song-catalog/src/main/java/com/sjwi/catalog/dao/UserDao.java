/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.sjwi.catalog.model.LogEntry;
import com.sjwi.catalog.model.SetListState;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.model.user.UserState;

public interface UserDao {

  public User getUser(String username);

  public void saveUser(CfUser user);

  public void saveUserAuthorities(String username, Collection<GrantedAuthority> authorities);

  public void changePassword(String username, String password);

  public String getPassword(String username);

  public List<CfUser> getAllActiveUsers();

  public boolean isUsernameTaken(String username);

  public List<String> getAllEnrollmentEmails();

  public Map<String, String> getUserPreferences(String username);

  public void setUserPreference(String preferenceKey, String preferenceValue, String name);

  public void disableUser(String userName);

  public void log(
      String username,
      String os,
      String ipAddress,
      String signature,
      String requestUrl,
      boolean standAloneMode,
      String protocol,
      String parameters);

  public List<LogEntry> getLogData();

  public void storeAccountRequest(String email);

  public List<String> getAccountRequestDetails(String email);

  public void createAnonymousUser(String token);

  public String getAnonymousUser(String tokenLink);

  public UserState getUserState(String name);

  public void setUserState(UserState state, String name);

  public void addUserState(UserState userState, String name);

  public void cleanBots();

  public SetListState getSetlistStateForUser(String user, int setId, boolean create);

  public void updateSetListSessionState(String user, int setId, SetListState existingSetState);

  public void removeSetListSessionState(String user, int setId);

  public Map<Integer, SetListState> getAllSetlistStatesForUser(String user);
}
