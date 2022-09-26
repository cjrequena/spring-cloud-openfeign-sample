package com.cjrequena.sample.exception.api;

import com.cjrequena.sample.exception.controller.ControllerException;
import org.springframework.http.HttpStatus;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
public class ConflictApiException extends ControllerException {
  public ConflictApiException() {
    super(HttpStatus.CONFLICT);
  }

  public ConflictApiException(String message) {
    super(HttpStatus.CONFLICT, message);
  }
}
