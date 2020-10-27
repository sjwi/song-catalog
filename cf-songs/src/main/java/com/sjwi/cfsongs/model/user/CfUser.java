package com.sjwi.cfsongs.model.user;

import java.util.Collection;
import java.util.Map;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CfUser extends User {
	/**
	 * 
	 */
	private static final long serialVersionUID = -8215868281708962565L;
	private final String firstName;
	private final String lastName;
	private final String email;
	private final String fullName;
	private final Map<String, String> preferences;
	
	public CfUser(String username, String firstName, String lastName, String email, String password,
			Collection<? extends GrantedAuthority> authorities, Map<String, String> preferences) {
		super(username,password,true,true,true,true,authorities);
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.fullName = firstName + " " + lastName;
		this.preferences = preferences;
	}

	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getEmail() {
		return email;
	}
	public String getFullName() {
		return fullName;
	}
	public Map<String, String> getPreferences(){
		return preferences;
	}
}
