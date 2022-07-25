/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.exception;

public class InvalidOperationException extends Exception {

  /** */
  private static final long serialVersionUID = -6685970104706433818L;

  public InvalidOperationException() {}

  public InvalidOperationException(String message) {
    super(message);
  }

  public InvalidOperationException(Throwable cause) {
    super(cause);
  }

  public InvalidOperationException(String message, Throwable cause) {
    super(message, cause);
  }

  public InvalidOperationException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
