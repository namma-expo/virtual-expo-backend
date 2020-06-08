package com.nammaexpo.expection;

import com.nammaexpo.models.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
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
}
