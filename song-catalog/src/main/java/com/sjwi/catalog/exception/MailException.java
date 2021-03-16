package com.sjwi.catalog.exception;

public class MailException extends Exception {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6685970104706433818L;

	public MailException() {
	}

	public MailException(String message) {
		super(message);
	}

	public MailException(Throwable cause) {
		super(cause);
	}

	public MailException(String message, Throwable cause) {
		super(message, cause);
	}

	public MailException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

}
