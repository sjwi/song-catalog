/* (C)2022 https://stephenky.com */
package com.sjwi.catalog.controller;

import com.sjwi.catalog.log.CustomLogger;
import com.sjwi.catalog.model.ExceptionResponse;
import com.sjwi.catalog.model.ExceptionResponse.ExceptionResponseCode;
import com.sjwi.catalog.service.OrganizationService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

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
}
