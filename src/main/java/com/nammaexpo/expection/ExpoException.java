package com.nammaexpo.expection;

import com.google.common.collect.ImmutableMap;
import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ExpoException extends RuntimeException {
  private final HttpStatus httpStatusCode;
  private final String errorCodeName;
  private final transient Map<String, Object> context;

  public ExpoException(ErrorCode errorCode, Map<String, Object> context) {
    super();
    this.httpStatusCode = errorCode.getResponseCode();
    this.errorCodeName = errorCode.name();
    this.context = context;
  }

  public static ExpoException error(ErrorCode errorCode) {
    return new ExpoException(errorCode, new HashMap<>());
  }

  public static ExpoException error(ErrorCode errorCode, Map<String, Object> context) {
    return new ExpoException(errorCode, context);
  }

  public static ExpoException error(ErrorCode errorCode, Throwable e) {
    String message = e.getCause() == null ? "" : e.getCause().toString();

    if (e instanceof ExpoException) {
      return (ExpoException) e;
    } else if (e.getCause() instanceof ExpoException) {
      return (ExpoException) e.getCause();
    } else {
      return new ExpoException(errorCode, ImmutableMap.of("message", message));
    }
  }
}
