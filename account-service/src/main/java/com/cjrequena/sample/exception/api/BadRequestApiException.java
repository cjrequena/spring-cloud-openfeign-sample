package com.cjrequena.sample.exception.api;

import com.cjrequena.sample.exception.controller.ControllerException;
import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class BadRequestApiException extends ControllerException {
  public BadRequestApiException() {
    super(HttpStatus.BAD_REQUEST);
  }

  public BadRequestApiException(String message) {
    super(HttpStatus.BAD_REQUEST, message);
  }
}
