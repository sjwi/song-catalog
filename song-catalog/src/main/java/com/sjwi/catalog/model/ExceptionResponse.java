/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ExceptionResponse {
  ExceptionResponseCode code;
  String message;

  public enum ExceptionResponseCode {
    BAD_REQUEST,
    UNAUTHORIZED,
    INTERNAL_SERVER_ERROR
  }
}
