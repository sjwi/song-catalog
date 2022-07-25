/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.dao;

import com.sjwi.catalog.model.security.EnrollmentToken;
import com.sjwi.catalog.model.security.PasswordResetToken;
import com.sjwi.catalog.model.security.SecurityToken;

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
