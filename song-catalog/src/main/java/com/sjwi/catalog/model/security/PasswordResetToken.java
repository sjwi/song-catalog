package com.sjwi.catalog.model.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;

public class PasswordResetToken extends StoredCookieToken {
	
	private final String sessionToken;

	public PasswordResetToken(int id, String user, String token, Date createdOn) {
		super(id, user, token, createdOn);
		sessionToken = null;
	}

	private PasswordResetToken(int id, String user, String token, Date createdOn, String sessionToken) {
		super(id, user, token, createdOn);
		this.sessionToken = sessionToken;
	}
	
	public PasswordResetToken(String user) throws InvalidKeySpecException, NoSuchAlgorithmException {
		super(user);
		sessionToken = null;
	}

	@Override
	public boolean isTokenValid() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_MONTH, -1);
		return sessionToken != null && token != null? createdOn.compareTo(cal.getTime()) > 0 && sessionToken.equals(token): false;
	}
	public PasswordResetToken setSessionToken(String sessionToken) {
		return new PasswordResetToken(this.id,this.user,this.token,this.createdOn,sessionToken);
	}
}
