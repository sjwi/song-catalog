package com.sjwi.catalog.exception;

public class PasswordException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6685970104706433818L;

	public PasswordException() {}

	public PasswordException(String message) {
		super(message);
	}

	public PasswordException(Throwable cause) {
		super(cause);
	}

	public PasswordException(String message, Throwable cause) {
		super(message, cause);
	}

	public PasswordException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
