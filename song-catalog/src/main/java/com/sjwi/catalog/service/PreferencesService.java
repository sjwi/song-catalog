package com.sjwi.catalog.service;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.dao.UserDao;

@Component
public class PreferencesService {

	@Autowired
	UserDao userDao;
	
	public Map<String, String> getUserPreferences(String username){
		return userDao.getUserPreferences(username);
	}

	public void setUserPreference(String preferenceKey, String preferenceValue, String name) {
		userDao.setUserPreference(preferenceKey,preferenceValue,name);
	}
}
