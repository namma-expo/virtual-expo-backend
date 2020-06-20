package com.nammaexpo.expection;

import com.nammaexpo.expection.ExpoException.ErrorCode;
import com.nammaexpo.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        .context(e.getContext())
        .build(),
        e.getHttpStatusCode());
  }

  @ExceptionHandler(value = {ConstraintViolationException.class,
      MethodArgumentNotValidException.class})
  public ResponseEntity<ErrorResponse> validationHandling(Exception e) {
    log.error("ERROR:: ", e);

    return new ResponseEntity<>(ErrorResponse.builder()
        .errorCode(ErrorCode.VALIDATION_FAILED.name())
        .build(),
        HttpStatus.BAD_REQUEST);
  }
}
