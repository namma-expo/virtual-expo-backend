package com.nammaexpo.expection;

import com.nammaexpo.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.sql.SQLIntegrityConstraintViolationException;

@Slf4j
@ControllerAdvice
public class ExpoExpectionMapper {

  @ExceptionHandler(value = {ExpoException.class})
  public ResponseEntity<ErrorResponse> toResponseEntity(ExpoException e) {
    log.error("ERROR:: {} {}", e.getErrorCodeName(), e.getContext(), e);

    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(e.getErrorCodeName())
            .message(e.getMessage())
            .context(e.getContext())
        .build(),
        e.getHttpStatusCode());
  }

  @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class, ConstraintViolationException.class})
  public ResponseEntity<ErrorResponse> bindingException(Exception exception) {
    log.error("ERROR:: {}", exception.getMessage(), exception);

    ErrorCode errorCode = ErrorCode.VALIDATION_FAILED;
    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(errorCode.name())
            .message(exception.getMessage())
            .build(), errorCode.getResponseCode());
  }

  @ExceptionHandler(value = {JpaSystemException.class})
  public ResponseEntity<ErrorResponse> jpaSystemException(Exception exception) {
    log.error("ERROR:: {}", exception.getMessage(), exception);

    ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(errorCode.name())
            .message(exception.getMessage())
            .build(), errorCode.getResponseCode());
  }

  @ExceptionHandler(value = {AccessDeniedException.class})
  public ResponseEntity<ErrorResponse> accessDenied(Exception exception) {
    log.error("ERROR:: {}", exception.getMessage(), exception);

    ErrorCode errorCode = ErrorCode.ACCESS_DENIED;
    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(errorCode.name())
            .message(exception.getMessage())
            .build(), errorCode.getResponseCode());
  }

  @ExceptionHandler(value = {MissingServletRequestParameterException.class, MissingRequestHeaderException.class})
  public ResponseEntity<ErrorResponse> missingParameter(Exception exception) {
    log.error("ERROR:: {}", exception.getMessage(), exception);

    ErrorCode errorCode = ErrorCode.REQUIRED_PARAMETER_NOT_FOUND;
    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(errorCode.name())
            .message(exception.getMessage())
            .build(), errorCode.getResponseCode());
  }

  @ExceptionHandler(value = {HttpRequestMethodNotSupportedException.class})
  public ResponseEntity<ErrorResponse> invalidRequestMethod(Exception exception) {
    log.error("ERROR:: {}", exception.getMessage(), exception);

    ErrorCode errorCode = ErrorCode.INVALID_METHOD_EXCEPTION;
    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(errorCode.name())
            .message(exception.getMessage())
            .build(), errorCode.getResponseCode());
  }

  @ExceptionHandler(value = {DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
  public ResponseEntity<ErrorResponse> constraintVoilation(Exception exception) {
    log.error("ERROR:: {}", exception.getMessage(), exception);

    ErrorCode errorCode = ErrorCode.DATABASE_ERROR;
    return new ResponseEntity<>(ErrorResponse.builder()
            .errorCode(errorCode.name())
            .message(exception.getMessage())
            .build(), errorCode.getResponseCode());
  }
}
