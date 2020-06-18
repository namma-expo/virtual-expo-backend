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
    ACCESS_DENIED(HttpStatus.FORBIDDEN),
    REQUIRED_PARAMETER_NOT_FOUND(HttpStatus.BAD_REQUEST),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST),
    UPDATE_CONTACT_FAILED(HttpStatus.INTERNAL_SERVER_ERROR),
    INVALID_METHOD_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED);

    @Getter
    HttpStatus responseCode;

    ErrorCode(HttpStatus responseCode) {
        this.responseCode = responseCode;
    }

}