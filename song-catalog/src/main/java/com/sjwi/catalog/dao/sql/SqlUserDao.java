package com.sjwi.catalog.dao.sql;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Repository;

import com.sjwi.catalog.dao.UserDao;
import com.sjwi.catalog.model.user.CfUser;
import com.sjwi.catalog.service.AddressBookService;

@Repository
public class SqlUserDao implements UserDao {

	@Autowired
	private Map<String,String> queryStore;
	
	@Autowired
	JdbcTemplate jdbcTemplate;
	
	@Autowired
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	AddressBookService addressBookService;
	
	@Override
	public User getUser(String username) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("username", username);
		return namedParameterJdbcTemplate.query(queryStore.get("getUserByUsername"),parameters,r -> {
			if (r.next()){
				return new CfUser(r.getString("username"),
						r.getString("firstname"),
						r.getString("lastname"),
						r.getString("email"),
						r.getString("password"),
						getUserAuthorities(username),
						getUserPreferences(username));
			} else {
				return getUserByEmail(username);
			}
		});
	}
	
	@Override
	public Map<String, String> getUserPreferences(String username) {
		return jdbcTemplate.query(queryStore.get("getPreferencesForUser"), new Object[] {username}, r -> {
			Map<String, String> preferences = new HashMap<>();
			while (r.next()) {
				preferences.put(r.getString("PreferenceKey"), r.getString("PreferenceValue"));
			}
			return preferences;
		});
	}

	@Override
	public void setUserPreference(String preferenceKey, String preferenceValue, String name) {
		jdbcTemplate.update(queryStore.get("deleteUserPreference"), new Object[] {name, preferenceKey});
		jdbcTemplate.update(queryStore.get("createUserPreference"), new Object[] {name,preferenceKey,preferenceValue});
	}

	private User getUserByEmail(String email) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put("email", email);
		return namedParameterJdbcTemplate.query(queryStore.get("getUserByEmail"),parameters,r -> {
			if (r.next()){
				return new CfUser(r.getString("username"),
						r.getString("firstname"),
						r.getString("lastname"),
						r.getString("email"),
						r.getString("password"),
						getUserAuthorities(r.getString("username")),
						getUserPreferences(r.getString("username")));
			} else {
				return null;
			}
		});
	}
	private Collection<? extends GrantedAuthority> getUserAuthorities(String username) {
		return jdbcTemplate.query(queryStore.get("getAuthoritiesByUsername"), new Object[] {username}, r -> {
			List <SimpleGrantedAuthority> authorities = new ArrayList<SimpleGrantedAuthority>();
			while (r.next()) {
				authorities.add(new SimpleGrantedAuthority(r.getString("authority")));
			}
			return authorities;
		});
	}
	
	@Override
	public void saveUser(CfUser user) {
		jdbcTemplate.update(queryStore.get("saveUser"),new Object[] {user.getUsername(),user.getPassword()});
	}
	
	@Override
	public void saveUserAuthorities(String username, Collection<GrantedAuthority> authorities) {
		authorities.stream().forEach(a -> {
			jdbcTemplate.update(queryStore.get("saveUserAuthorities"), new Object[] {username, a.getAuthority()});
		});
	}

	@Override
	public void changePassword(String username, String password) {
		jdbcTemplate.update(queryStore.get("changePassword"), new Object[] {password,username});
	}

	@Override
	public String getPassword(String username) {
		return jdbcTemplate.query(queryStore.get("getUserPassword"), new Object[] {username},r ->{
			r.next();
			return r.getString("password");
		});
	}
	
	@Override
	public List<CfUser> getAllActiveUsers(){
		return jdbcTemplate.query(queryStore.get("getAllActiveUsers"), r -> {
			List<CfUser> users = new ArrayList<CfUser>();
			while (r.next()) {
				users.add(new CfUser(r.getString("username"),
						r.getString("firstname"),
						r.getString("lastname"),
						r.getString("email"),
						r.getString("password"),
						getUserAuthorities(r.getString("username")),
						getUserPreferences(r.getString("username"))
						));
			}
			return users;
		});
	}

	@Override
	public boolean isUsernameTaken(String username) {
		return jdbcTemplate.query(queryStore.get("getActiveUsersByUsername"), new Object[] {username}, r->{
			return r.next();
		});
	}

	@Override
	public List<String> getAllEnrollmentEmails() {
		return jdbcTemplate.query(queryStore.get("getAllEnrollmentTokens"), r -> {
			List<String> tokens = new ArrayList<String>();
			while (r.next()) {
				tokens.add(r.getString("EMAIL"));
			}
			return tokens;
		});
	}
	
	@Override
	public void disableUser(String userName) {
		jdbcTemplate.update(queryStore.get("disableUser"), new Object[] {userName});
		jdbcTemplate.update(queryStore.get("purgeTokensForUser"), new Object[] {userName});
	}
}