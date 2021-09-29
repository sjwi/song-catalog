package com.sjwi.catalog.dao;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.sjwi.catalog.model.Log;
import com.sjwi.catalog.model.user.CfUser;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

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

  public void log(String username, String os, String signature, String requestUrl, String parameters);

	public List<Log> getLogData();
}