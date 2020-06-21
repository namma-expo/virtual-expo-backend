package com.nammaexpo.expection;

import com.google.common.collect.ImmutableMap;
import com.nammaexpo.models.enums.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

@Getter
public class ExpoException extends RuntimeException {

    private final HttpStatus httpStatusCode;
    private final String errorCode;
    private final String errorMessage;
    private final transient Map<String, Object> context;

    public ExpoException(MessageCode messageCode, Map<String, Object> context) {
        super();
        this.httpStatusCode = messageCode.getResponseCode();
        this.errorCode = messageCode.name();
        this.errorMessage = messageCode.getResponseMessage();
        this.context = context;
    }

    public static ExpoException error(MessageCode messageCode) {
        return new ExpoException(messageCode, new HashMap<>());
    }

    public static ExpoException error(MessageCode messageCode, Map<String, Object> context) {
        return new ExpoException(messageCode, context);
    }

    public static ExpoException error(MessageCode messageCode, Throwable e) {
        String message = e.getCause() == null ? "" : e.getCause().toString();

        if (e instanceof ExpoException) {
            return (ExpoException) e;
        } else if (e.getCause() instanceof ExpoException) {
            return (ExpoException) e.getCause();
        } else {
            return new ExpoException(messageCode, ImmutableMap.of("message", message));
        }
    }
}
