package com.nammaexpo.expection;

import lombok.Getter;
import org.springframework.http.HttpStatus;

public enum ErrorCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    USER_NAME_EXIST(HttpStatus.BAD_REQUEST),
    EMAIL_IN_USE(HttpStatus.BAD_REQUEST),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST),
    UNREGISTERED_USER(HttpStatus.NOT_FOUND),
    INVALID_USER_NAME_PASSWORD(HttpStatus.FORBIDDEN),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN),
    TOKEN_VERIFICATION_FAILED(HttpStatus.FORBIDDEN),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR),
    ACCESS_DENIED(HttpStatus.FORBIDDEN);

    @Getter
    HttpStatus responseCode;

    ErrorCode(HttpStatus responseCode) {
        this.responseCode = responseCode;
    }

}