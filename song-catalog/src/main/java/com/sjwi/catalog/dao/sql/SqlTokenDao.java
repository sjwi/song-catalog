package com.sjwi.catalog.dao.sql;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.sjwi.catalog.dao.TokenDao;
import com.sjwi.catalog.model.security.EnrollmentToken;
import com.sjwi.catalog.model.security.PasswordResetToken;
import com.sjwi.catalog.model.security.SecurityToken;
import com.sjwi.catalog.model.security.StoredCookieToken;

@Component
public class SqlTokenDao implements TokenDao {

	@Autowired
	JdbcTemplate jdbcTemplate;

	@Autowired
	private Map<String,String> queryStore;
	
	@Override
	public void storeEnrollmentToken(EnrollmentToken token) {
		jdbcTemplate.update(queryStore.get("storeToken"), new Object[] {token.getTokenString(), token.getUser(), token.getRole()});
	}

	@Override
	public EnrollmentToken getEnrollmentToken(String user) {
		return jdbcTemplate.query(queryStore.get("getTokenByUser"), new Object[] {user}, r -> {
			if (r.next()) {
				return new EnrollmentToken(r.getInt("ID"),r.getString("TOKEN"), r.getString("EMAIL"),r.getString("PRIVILEGE"));
			} else {
				return null;
			}
		});
	}

	@Override
	public void deleteEnrollmentToken(int id) {
		jdbcTemplate.update(queryStore.get("deleteTokenById"),new Object[] {id});
	}

	@Override
	public SecurityToken getStoredCookieToken(String token) {
		return jdbcTemplate.query(queryStore.get("getCookieToken"), new Object[] {token}, r -> {
			if (r.next()) {
				return new StoredCookieToken(r.getInt("ID"), r.getString("username"),r.getString("LOGIN_COOKIE"),r.getDate("CREATED_ON"));
			} else {
				return null;
			}
		});
		
	}

	@Override
	public void storeCookieToken(SecurityToken token) {
		jdbcTemplate.update(queryStore.get("storeCookieToken"), new Object[] {token.getUser(),token.getTokenString()});
	}

	@Override
	public void deleteCookieToken(String token) {
		jdbcTemplate.update(queryStore.get("deleteCookieToken"), new Object[] {token});
	}

	@Override
	public void storePasswordResetToken(SecurityToken token) {
		jdbcTemplate.update(queryStore.get("deletePasswordResetToken"), new Object[] {token.getUser()});
		jdbcTemplate.update(queryStore.get("storePasswordResetToken"), new Object[] {token.getUser(),token.getTokenString()});
	}

	@Override
	public PasswordResetToken getPasswordResetToken(String username) {
		return jdbcTemplate.query(queryStore.get("getPasswordResetToken"), new Object[] {username}, r -> {
			if (r.next()) {
				return new PasswordResetToken(r.getInt("ID"), r.getString("username"),r.getString("RESET_TOKEN"),r.getDate("CREATED_ON"));
			} else {
				return null;
			}
		});
	}

	@Override
	public void deletePasswordResetToken(String username) {
		jdbcTemplate.update(queryStore.get("deletePasswordResetToken"),new Object[] {username});
	}
}
