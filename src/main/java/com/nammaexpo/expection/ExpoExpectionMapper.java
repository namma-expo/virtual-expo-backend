package com.nammaexpo.expection;

import com.google.common.collect.ImmutableMap;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.response.MessageResponse;
import io.jsonwebtoken.MalformedJwtException;
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

    public static final String ERROR_MESSAGE = "ERROR:: {}";

    @ExceptionHandler(value = {ExpoException.class})
    public ResponseEntity<MessageResponse> handleExpoException(ExpoException e) {
        return error(e.getHttpStatusCode(), e);
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    public ResponseEntity<MessageResponse> bindingException(Exception exception) {
        return error(VALIDATION_FAILED, exception);
    }

    @ExceptionHandler(value = {JpaSystemException.class})
    public ResponseEntity<MessageResponse> jpaSystemException(Exception exception) {
        return error(INTERNAL_SERVER_ERROR, exception);
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<MessageResponse> accessDenied(Exception exception) {
        return error(ACCESS_DENIED, exception);
    }

    @ExceptionHandler(value = {MalformedJwtException.class})
    public ResponseEntity<MessageResponse> invalidToken(Exception exception) {
        return error(TOKEN_VERIFICATION_FAILED, exception);
    }

    @ExceptionHandler(value = {DataIntegrityViolationException.class, SQLIntegrityConstraintViolationException.class})
    public ResponseEntity<MessageResponse> constraintVoilation(Exception exception) {
        return error(DATABASE_ERROR, exception);
    }

    private ResponseEntity<MessageResponse> error(HttpStatus httpStatusCode, ExpoException e) {
        log.error(ERROR_MESSAGE, e.getMessage(), e);
        return new ResponseEntity<>(MessageResponse.builder()
                        .messageCode(e.getMessageCode())
                        .context(e.getContext())
                        .build(), httpStatusCode);
    }

    private ResponseEntity<MessageResponse> error(MessageCode messageCode, Exception e) {
        log.error(ERROR_MESSAGE, e.getMessage(), e);
        List<String> list = new ArrayList<>();
        list.add(e.getLocalizedMessage());
        return new ResponseEntity<>(MessageResponse.builder()
                .messageCode(messageCode)
                .context(ImmutableMap.of("error", list))
                .build(), messageCode.getStatusCode());
    }
}
