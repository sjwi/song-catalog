package com.sjwi.cfsongs.service;

import javax.servlet.http.Cookie;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sjwi.cfsongs.dao.TokenDao;
import com.sjwi.cfsongs.model.security.EnrollmentToken;
import com.sjwi.cfsongs.model.security.PasswordResetToken;
import com.sjwi.cfsongs.model.security.SecurityToken;

@Component
public class TokenService {

	@Autowired
	TokenDao tokenDao;
	
	public SecurityToken getStoredCookieToken(String token) {
		return tokenDao.getStoredCookieToken(token);
	}
	
	public void storeEnrollmentToken(EnrollmentToken token) {
		tokenDao.storeEnrollmentToken(token);
	}

	public EnrollmentToken getEnrollmentToken(String user) {
		return tokenDao.getEnrollmentToken(user);
	}

	public void deleteEnrollmentToken(int id) {
		tokenDao.deleteEnrollmentToken(id);
	}

	public void storeCookieToken(SecurityToken token) {
		tokenDao.storeCookieToken(token);
	}

	public void deleteCookieToken(Cookie cookieToken) {
		if (cookieToken != null) {
			tokenDao.deleteCookieToken(cookieToken.getValue());
		}
	}

	public void storePasswordResetToken(SecurityToken token) {
		tokenDao.storePasswordResetToken(token);
	}
	public PasswordResetToken getPasswordResetToken(String username) {
		return tokenDao.getPasswordResetToken(username);
	}

	public void deletePasswordResetToken(String username) {
		tokenDao.deletePasswordResetToken(username);
	}
}
