package com.cjrequena.sample.web.controller;

import org.springframework.http.HttpStatus;

/**
 * @author cjrequena
 *
 */
public class ConflictControllerException extends ControllerException {
  public ConflictControllerException() {
    super(HttpStatus.CONFLICT, HttpStatus.CONFLICT.getReasonPhrase());
  }

  public ConflictControllerException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
