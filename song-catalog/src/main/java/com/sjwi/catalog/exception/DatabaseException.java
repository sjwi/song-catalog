/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.exception;

public class DatabaseException extends Exception {

  /** */
  private static final long serialVersionUID = -6685970104706433818L;

  public DatabaseException() {}

  public DatabaseException(String message) {
    super(message);
  }

  public DatabaseException(Throwable cause) {
    super(cause);
  }

  public DatabaseException(String message, Throwable cause) {
    super(message, cause);
  }

  public DatabaseException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
