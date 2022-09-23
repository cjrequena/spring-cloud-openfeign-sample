package com.cjrequena.sample.exception;

import com.cjrequena.sample.exception.api.ApiException;
import com.cjrequena.sample.exception.service.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * <p></p>
 * <p></p>
 * @author cjrequena
 */
@RestControllerAdvice
@Slf4j
public class CustomExceptionHandler {

  public static final String DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm";
  private static final String EXCEPTION_LOG = "Exception {}";

  @ExceptionHandler({Exception.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<Object> unhandledExceptions(Exception ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
  }

  @ExceptionHandler({ConstraintViolationException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<Object> handleConstrainViolationException(ConstraintViolationException ex) {
    log.debug(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
  }

  @ExceptionHandler({ServerWebInputException.class})
  @ResponseStatus(value = HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<Object> handleServerWebInputException(ServerWebInputException ex) {
    log.debug(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.BAD_REQUEST.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorDTO);
  }

  @ExceptionHandler({ServiceException.class})
  @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
  @ResponseBody
  public ResponseEntity<Object> handleServiceException(ServiceException ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorDTO);
  }

  @ExceptionHandler({ApiException.class})
  @ResponseBody
  public ResponseEntity<Object> handleApiException(ApiException ex) {
    log.error(EXCEPTION_LOG, ex.getMessage(), ex);
    ErrorDTO errorDTO = new ErrorDTO();
    errorDTO.setDate(LocalDateTime.now().format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT)));
    errorDTO.setStatus(ex.getHttpStatus().value());
    errorDTO.setErrorCode(ex.getClass().getSimpleName());
    errorDTO.setMessage(ex.getMessage());
    return ResponseEntity.status(ex.getHttpStatus()).body(errorDTO);
  }

}
