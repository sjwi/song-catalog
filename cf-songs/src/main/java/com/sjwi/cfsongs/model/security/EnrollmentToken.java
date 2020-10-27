package com.sjwi.cfsongs.model.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

public class EnrollmentToken extends SecurityToken {

	private final String role;
	private final String sessionToken;

	public EnrollmentToken(int id,String user, String token, String role) {
		super(id,token,user);
		this.role = role;
		sessionToken = null;
	}

	private EnrollmentToken(int id,String user, String token, String role, String requestToken) {
		super(id,token,user);
		this.role = role;
		this.sessionToken = requestToken;
	}
	
	public EnrollmentToken(String user, String role) throws InvalidKeySpecException, NoSuchAlgorithmException {
		super(user);
		this.role = role;
		sessionToken = null;
	}
	
	public EnrollmentToken setSessionToken(String sessionToken) {
		return new EnrollmentToken(this.id,this.token,this.user,this.role,sessionToken);
	}

	public String getRole() {
		return role;
	}

	@Override
	public boolean isTokenValid() {
		return sessionToken == null || token == null ? false : 
					sessionToken.equals(token);
	}
}