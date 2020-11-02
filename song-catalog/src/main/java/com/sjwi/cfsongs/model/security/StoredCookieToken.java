package com.sjwi.cfsongs.model.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Calendar;
import java.util.Date;

public class StoredCookieToken extends SecurityToken {
	
	public static final String STORED_COOKIE_TOKEN_KEY = "USESSIONID";
	
	protected final Date createdOn;

	public StoredCookieToken(int id, String user, String token, Date createdOn) {
		super(id, user, token);
		this.createdOn = createdOn;
	}
	
	public StoredCookieToken(String user) throws InvalidKeySpecException, NoSuchAlgorithmException {
		super(user);
		this.createdOn = new Date();
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	@Override
	public boolean isTokenValid() {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, -1);
		return createdOn.compareTo(cal.getTime()) > 0;
	}
}
