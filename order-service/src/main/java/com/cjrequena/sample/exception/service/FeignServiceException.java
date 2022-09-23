package com.cjrequena.sample.exception.service;

import org.springframework.lang.Nullable;

public class FeignServiceException extends ServiceException {

  public FeignServiceException(String message) {
    super(message);
  }

  public FeignServiceException(String message, @Nullable Throwable cause) {
    super(message);
  }
}
