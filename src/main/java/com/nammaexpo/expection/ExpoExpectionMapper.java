package com.nammaexpo.expection;

import com.google.common.collect.ImmutableMap;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.response.MessageResponse;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import static com.nammaexpo.models.enums.MessageCode.*;

@Slf4j
@SuppressWarnings({"unchecked","rawtypes"})
@RestControllerAdvice
public class ExpoExpectionMapper {


    @ExceptionHandler(value = {ExpoException.class})
    public ResponseEntity<Object> handleExpoException(ExpoException e) {
        log.error("ERROR:: {}", e.getMessage(), e);
        return error(e.getHttpStatusCode(), e);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<Object> bindingException(Exception exception) {
        log.error("ERROR:: {}", exception.getMessage(), exception);
        return error(VALIDATION_FAILED, exception);
    }

    @ExceptionHandler(value = {JpaSystemException.class})
    public ResponseEntity<Object> jpaSystemException(Exception exception) {
        log.error("ERROR:: {}", exception.getMessage(), exception);
        return error(INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<Object> accessDenied(Exception exception) {
        log.error("ERROR:: {}", exception.getMessage(), exception);
        return error(ACCESS_DENIED, exception);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<Object> constraintVoilation(Exception exception) {
        log.error("ERROR:: {}", exception.getMessage(), exception);
        return error(DATABASE_ERROR, exception);
    }

    private ResponseEntity<Object> error(HttpStatus httpStatusCode, ExpoException e) {
        return new ResponseEntity(MessageResponse.builder()
                        .messageCode(e.getMessageCode())
                        .context(e.getContext())
                        .build(), httpStatusCode);
    }

    private ResponseEntity<Object> error(MessageCode messageCode, Exception e) {
        List<String> list = new ArrayList<>();
        list.add(e.getLocalizedMessage());
        return new ResponseEntity(MessageResponse.builder()
                        .messageCode(messageCode)
                        .context(ImmutableMap.of("error", list))
                        .build(), messageCode.getStatusCode());
    }
}
