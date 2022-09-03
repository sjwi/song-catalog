/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.exception;

public class UnauthorizedException extends RuntimeException {

  public UnauthorizedException(String msg) {
    super(msg);
  }
}
