package com.sjwi.catalog.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.sjwi.catalog.dao.UserDao;
import com.sjwi.catalog.exception.PasswordException;
import com.sjwi.catalog.model.addressbook.AddressBookEntry;
import com.sjwi.catalog.model.user.CfUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service("userDetailsService")
public class UserService implements UserDetailsService {
	
	@Autowired
	AddressBookService addressBookService;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	UserDao userDao;

	private static Map<String,CfUser> cachedUsers = new HashMap<>();

	public CfUser createUser(String username, String firstName, String lastName, String email, String password, List<String> authorityNames, Map<String, String> preferences) {
		password = passwordEncoder.encode(password);
	    List <SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
	    authorityNames.stream().forEach(a -> {
			authorities.add(new SimpleGrantedAuthority(a.toUpperCase()));
	    });
	    CfUser user = new CfUser(username.trim().toLowerCase(), firstName, lastName, email, password, authorities, preferences);
	    updateAddressBookEntryForUser(user);
	    userDao.saveUser(user);
		userDao.saveUserAuthorities(user.getUsername(),user.getAuthorities());
	    return user;
	}

	private void updateAddressBookEntryForUser(CfUser user) {
		AddressBookEntry existingUser = addressBookService.getAddressBookEntryByEmail(user.getEmail());
		if (existingUser == null) {
			addressBookService.createEntry(new AddressBookEntry(0,user.getFirstName(),user.getLastName(),
					user.getUsername(),user.getEmail(),null));
		} else {
			addressBookService.editEntryByEmail(new AddressBookEntry(0,user.getFirstName(),
					user.getLastName(),user.getUsername(),user.getEmail(),null));
		}
	}

	public void changePassword(String username, String oldPassword, String newPassword) throws PasswordException {
		if (!passwordEncoder.matches(oldPassword,userDao.getPassword(username))) {
			throw new PasswordException("Bad password");
		}
		newPassword = passwordEncoder.encode(newPassword);
		userDao.changePassword(username.trim().toLowerCase(),newPassword);
	}

	public void resetPassword(String username, String newPassword) throws PasswordException {
		newPassword = passwordEncoder.encode(newPassword);
		userDao.changePassword(username.trim().toLowerCase(),newPassword);
	}

	@Override
	public UserDetails loadUserByUsername(final String username) throws UsernameNotFoundException {
		return userDao.getUser(username.trim().toLowerCase());
	}

	public synchronized CfUser loadCfUserByUsername(String username) throws UsernameNotFoundException {
		username = username.trim().toLowerCase();
		if (cachedUsers.containsKey(username)){
			return cachedUsers.get(username);
		} else {
			CfUser user = (CfUser) userDao.getUser(username);
			cachedUsers.put(username,user);
			return user;
		}
	}
	
	public List<CfUser> getAllActiveUsers() {
		return userDao.getAllActiveUsers();
	}
	
	public List<String> getAllEnrollmentEmails() {
		return userDao.getAllEnrollmentEmails();
	}
	
	public boolean isUsernameTaken(String username) {
		return userDao.isUsernameTaken(username);
	}

	public void logUserAction(String username, String os, String signature, String requestUrl, String parameters) {
		userDao.log(username, os, signature, requestUrl, parameters);
  }
}
	
