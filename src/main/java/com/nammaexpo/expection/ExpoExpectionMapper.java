package com.nammaexpo.expection;

import com.nammaexpo.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

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

  @ExceptionHandler(value = {BindException.class, MethodArgumentNotValidException.class})
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

}
