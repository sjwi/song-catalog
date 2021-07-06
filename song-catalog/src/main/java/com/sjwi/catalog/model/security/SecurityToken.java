package com.sjwi.catalog.model.security;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang.RandomStringUtils;

public abstract class SecurityToken {
	private final static int HASH_ITERATIONS = 10001;
	private final static int HASH_KEY_LENGTH = 529;

	protected final int id;
	protected final String token;
	protected final String user;

	public SecurityToken(int id, String user, String token) {
		this.id = id;
		this.user = user.toLowerCase();
		this.token = token;
	}
	
	public SecurityToken(String user) throws InvalidKeySpecException, NoSuchAlgorithmException {
		this.id = 0;
		this.user = user;
		this.token = generateTokenForUser(user);
	}

	public abstract String getTokenLink();
	
	public abstract boolean isTokenValid();

	public int getId() {
		return id;
	}
	public String getTokenString() {
		return token;
	}

	public String getUser() {
		return user;
	}
	private String generateTokenForUser(String user) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return Base64.getEncoder()
					.encodeToString(hashKeyWithSalt(user, RandomStringUtils.randomAlphanumeric(1000)).getBytes());
	}
	private static String hashKeyWithSalt(String key, String salt) throws InvalidKeySpecException, NoSuchAlgorithmException {
		return Hex.encodeHexString(getEncodedByteHashWithSalt(key.toCharArray(), salt.getBytes(), HASH_ITERATIONS, HASH_KEY_LENGTH));
	}

	private static byte[] getEncodedByteHashWithSalt(final char[] key, final byte[] salt, final int iterations, final int keyLength) throws InvalidKeySpecException, NoSuchAlgorithmException {

		return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA512").generateSecret(new PBEKeySpec(key, salt, iterations, keyLength)).getEncoded();
	}
}
