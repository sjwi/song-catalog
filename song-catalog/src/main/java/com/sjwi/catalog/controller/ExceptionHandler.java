/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sjwi.catalog.exception.UnauthorizedException;
import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.ExceptionResponse;
import com.sjwi.catalog.model.ExceptionResponse.ExceptionResponseCode;
import com.sjwi.catalog.service.OrganizationService;

import lombok.extern.java.Log;

@ControllerAdvice
@Log
public class ExceptionHandler extends ResponseEntityExceptionHandler {

  @Autowired OrganizationService organizationService;

  @Autowired CustomLogger customLogger;

  @org.springframework.web.bind.annotation.ExceptionHandler({Exception.class})
  public ResponseEntity<ExceptionResponse> exceptionHandler(Exception e) {
    customLogger.logErrorWithEmail(e.getMessage());
    logger.error("An unknown error occured", e);
    return ResponseEntity.internalServerError()
        .body(
            new ExceptionResponse(
                ExceptionResponseCode.INTERNAL_SERVER_ERROR, "Unable to process your request"));
  }

  @org.springframework.web.bind.annotation.ExceptionHandler({UnauthorizedException.class})
  public ResponseEntity<ExceptionResponse> unauthorizedExceptionHandler(UnauthorizedException e) {
    return new ResponseEntity<>(new ExceptionResponse(ExceptionResponseCode.UNAUTHORIZED, e.getMessage()), HttpStatus.UNAUTHORIZED);
  }
}
