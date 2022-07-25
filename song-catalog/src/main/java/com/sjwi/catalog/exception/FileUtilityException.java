/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.exception;

public class FileUtilityException extends Exception {

  /** */
  private static final long serialVersionUID = -6685970104706433818L;

  public FileUtilityException() {}

  public FileUtilityException(String message) {
    super(message);
  }

  public FileUtilityException(Throwable cause) {
    super(cause);
  }

  public FileUtilityException(String message, Throwable cause) {
    super(message, cause);
  }

  public FileUtilityException(
      String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
