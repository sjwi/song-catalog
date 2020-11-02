package com.sjwi.cfsongs.dao;

import com.sjwi.cfsongs.model.security.EnrollmentToken;
import com.sjwi.cfsongs.model.security.PasswordResetToken;
import com.sjwi.cfsongs.model.security.SecurityToken;

public interface TokenDao {

	public void storeEnrollmentToken(EnrollmentToken token);

	public EnrollmentToken getEnrollmentToken(String user);

	public void deleteEnrollmentToken(int id);

	public SecurityToken getStoredCookieToken(String token);

	public void storeCookieToken(SecurityToken token);

	public void deleteCookieToken(String cookie);

	public void storePasswordResetToken(SecurityToken token);

	public PasswordResetToken getPasswordResetToken(String username);

	public void deletePasswordResetToken(String username);
}
