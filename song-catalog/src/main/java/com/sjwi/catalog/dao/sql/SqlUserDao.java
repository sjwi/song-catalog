/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao.sql;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import com.sjwi.catalog.dao.UserDao;
import com.sjwi.catalog.model.LogEntry;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.model.user.UserState;
import com.sjwi.catalog.service.AddressBookService;

@Repository
public class SqlUserDao implements UserDao {

  @Autowired private Map<String, String> queryStore;

  @Autowired JdbcTemplate jdbcTemplate;

  @Autowired NamedParameterJdbcTemplate namedParameterJdbcTemplate;

  @Autowired AddressBookService addressBookService;

  private final Calendar tzCal = Calendar.getInstance(TimeZone.getTimeZone("GMT-3"));

  @Override
  public User getUser(String username) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("username", username);
    return namedParameterJdbcTemplate.query(
        queryStore.get("getUserByUsername"),
        parameters,
        r -> {
          if (r.next()) {
            return new CfUser(
                r.getString("username"),
                r.getString("firstname"),
                r.getString("lastname"),
                r.getString("email"),
                r.getString("password"),
                getUserAuthorities(username),
                getUserPreferences(username));
          } else {
            return getUserByEmail(username);
          }
        });
  }

  @Override
  public Map<String, String> getUserPreferences(String username) {
    return jdbcTemplate.query(
        queryStore.get("getPreferencesForUser"),
        new Object[] {username},
        r -> {
          Map<String, String> preferences = new HashMap<>();
          while (r.next()) {
            preferences.put(r.getString("PreferenceKey"), r.getString("PreferenceValue"));
          }
          return preferences;
        });
  }

  @Override
  public void setUserPreference(String preferenceKey, String preferenceValue, String name) {
    jdbcTemplate.update(queryStore.get("deleteUserPreference"), new Object[] {name, preferenceKey});
    jdbcTemplate.update(
        queryStore.get("createUserPreference"),
        new Object[] {name, preferenceKey, preferenceValue});
  }

  private User getUserByEmail(String email) {
    Map<String, String> parameters = new HashMap<>();
    parameters.put("email", email);
    return namedParameterJdbcTemplate.query(
        queryStore.get("getUserByEmail"),
        parameters,
        r -> {
          if (r.next() && r.getString("username") != null) {
            return new CfUser(
                r.getString("username"),
                r.getString("firstname"),
                r.getString("lastname"),
                r.getString("email"),
                r.getString("password"),
                getUserAuthorities(r.getString("username")),
                getUserPreferences(r.getString("username")));
          } else {
            return null;
          }
        });
  }

  private Collection<? extends GrantedAuthority> getUserAuthorities(String username) {
    return jdbcTemplate.query(
        queryStore.get("getAuthoritiesByUsername"),
        new Object[] {username},
        r -> {
          List<SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
          while (r.next()) {
            authorities.add(new SimpleGrantedAuthority(r.getString("authority")));
          }
          return authorities;
        });
  }

  @Override
  public void saveUser(CfUser user) {
    jdbcTemplate.update(
        queryStore.get("saveUser"), new Object[] {user.getUsername(), user.getPassword()});
  }

  @Override
  public void saveUserAuthorities(String username, Collection<GrantedAuthority> authorities) {
    authorities.stream()
        .forEach(
            a -> {
              jdbcTemplate.update(
                  queryStore.get("saveUserAuthorities"), new Object[] {username, a.getAuthority()});
            });
  }

  @Override
  public void changePassword(String username, String password) {
    jdbcTemplate.update(queryStore.get("changePassword"), new Object[] {password, username});
  }

  @Override
  public String getPassword(String username) {
    return jdbcTemplate.query(
        queryStore.get("getUserPassword"),
        new Object[] {username},
        r -> {
          r.next();
          return r.getString("password");
        });
  }

  @Override
  public List<CfUser> getAllActiveUsers() {
    return jdbcTemplate.query(
        queryStore.get("getAllActiveUsers"),
        r -> {
          List<CfUser> users = new ArrayList<CfUser>();
          while (r.next()) {
            users.add(
                new CfUser(
                    r.getString("username"),
                    r.getString("firstname"),
                    r.getString("lastname"),
                    r.getString("email"),
                    r.getString("password"),
                    getUserAuthorities(r.getString("username")),
                    getUserPreferences(r.getString("username"))));
          }
          return users;
        });
  }

  @Override
  public boolean isUsernameTaken(String username) {
    return jdbcTemplate.query(
        queryStore.get("getActiveUsersByUsername"),
        new Object[] {username},
        r -> {
          return r.next();
        });
  }

  @Override
  public List<String> getAllEnrollmentEmails() {
    return jdbcTemplate.query(
        queryStore.get("getAllEnrollmentTokens"),
        r -> {
          List<String> tokens = new ArrayList<String>();
          while (r.next()) {
            tokens.add(r.getString("EMAIL"));
          }
          return tokens;
        });
  }

  @Override
  public void disableUser(String userName) {
    jdbcTemplate.update(queryStore.get("disableUser"), new Object[] {userName});
    jdbcTemplate.update(queryStore.get("purgeTokensForUser"), new Object[] {userName});
  }

  @Override
  @Async
  public void log(
      String username,
      String os,
      String ipAddress,
      String signature,
      String requestUrl,
      boolean standAlone,
      String protocol,
      String parameters) {
    jdbcTemplate.update(
        queryStore.get("log"),
        new Object[] {
          username, os, ipAddress, signature, requestUrl, standAlone, protocol, parameters
        });
  }

  @Override
  public List<LogEntry> getLogData() {
    return jdbcTemplate.query(
        queryStore.get("getLogData"),
        r -> {
          List<LogEntry> logs = new ArrayList<>();
          while (r.next())
            logs.add(
                new LogEntry(
                    r.getInt("ID"),
                    r.getTimestamp("ACTION_TIMESTAMP", tzCal),
                    r.getString("LEVEL"),
                    r.getString("USERNAME"),
                    r.getString("DEVICE"),
                    r.getString("IP"),
                    r.getString("METHOD"),
                    r.getString("REQ_URL"),
                    r.getBoolean("STAND_ALONE_MODE"),
                    r.getString("PROTOCOL"),
                    r.getString("PARAMS").split(";")));
          return logs;
        });
  }

  @Override
  public void storeAccountRequest(String email) {
    jdbcTemplate.update(queryStore.get("storeAccountRequest"), new Object[] {email});
  }

  @Override
  public List<String> getAccountRequestDetails(String email) {
    return jdbcTemplate.query(
        queryStore.get("getAccountRequestDetails"),
        new Object[] {email},
        r -> {
          List<String> accounts = new ArrayList<>();
          while (r.next()) accounts.add(r.getString("EMAIL"));
          return accounts;
        });
  }

  @Override
  public void createAnonymousUser(String token) {
    jdbcTemplate.update(queryStore.get("insertAnonymousUser"), new Object[] {token});
  }

  @Override
  public String getAnonymousUser(String tokenString) {
    return jdbcTemplate.query(
        queryStore.get("getAnonymousUser"),
        r -> {
          if (r.next()) return "user_" + r.getInt("ID");
          createAnonymousUser(tokenString);
          return getAnonymousUser(tokenString);
        },
        tokenString);
  }

  @Override
  public UserState getUserState(String name) {
    return jdbcTemplate.query(
        queryStore.get("getUserState"),
        r -> {
          if (r.next())
            return new UserState(
                r.getInt("LAST_SET_ORG"), r.getInt("LAST_SET_SERVICE"), r.getInt("LAST_SET_GROUP"));
          else return null;
        },
        name);
  }

  @Override
  public void setUserState(UserState state, String username) {
    Map<String, Object> parameterMap =
        Map.of(
            "username",
            username,
            "lastOrg",
            state.getLastOrg(),
            "lastService",
            state.getLastService(),
            "lastGroup",
            state.getLastGroup());
    namedParameterJdbcTemplate.update(queryStore.get("updateUserState"), parameterMap);
  }

  @Override
  public void addUserState(UserState state, String username) {
    Map<String, Object> parameterMap =
        Map.of(
            "username",
            username,
            "lastOrg",
            state.getLastOrg(),
            "lastService",
            state.getLastService(),
            "lastGroup",
            state.getLastGroup());
    namedParameterJdbcTemplate.update(queryStore.get("addUserState"), parameterMap);
  }

  @Override
  public void cleanBots() {
    jdbcTemplate.update(queryStore.get("cleanOldLogs"));
    jdbcTemplate.update(queryStore.get("makeBotTable"));
    jdbcTemplate.update(queryStore.get("cleanBotLogs"));
    jdbcTemplate.update(queryStore.get("makeBotTable"));
    jdbcTemplate.update(queryStore.get("cleanBots"));
  }
}
