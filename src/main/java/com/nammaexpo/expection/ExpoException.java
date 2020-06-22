package com.nammaexpo.expection;

import com.google.common.collect.ImmutableMap;
import com.nammaexpo.models.enums.MessageCode;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ExpoException extends RuntimeException {

    private final HttpStatus httpStatusCode;
    private final transient Map<String, Object> context;
    private final MessageCode messageCode;

    public ExpoException(MessageCode messageCode, Map<String, Object> context) {
        super(messageCode.getMessage());
        this.httpStatusCode = messageCode.getStatusCode();
        this.messageCode = messageCode;
        this.context = context;
    }

    public static ExpoException error(MessageCode messageCode) {
        return new ExpoException(messageCode, new HashMap<>());
    }

    public static ExpoException error(MessageCode messageCode, Map<String, Object> context) {
        return new ExpoException(messageCode, context);
    }

    public static ExpoException error(MessageCode messageCode, Throwable e) {
        if (e instanceof ExpoException) {
            return (ExpoException) e;
        } else if (e.getCause() instanceof ExpoException) {
            return (ExpoException) e.getCause();
        } else {
            List<String> list = new ArrayList<>();
            list.add(e.getLocalizedMessage());
            return new ExpoException(messageCode, ImmutableMap.of("error", list));
        }
    }
}
