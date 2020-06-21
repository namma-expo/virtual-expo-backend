package com.nammaexpo.models.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.util.HashMap;
import java.util.Map;

public enum MessageCode {
    INTERNAL_SERVER_ERROR(1, HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
    USER_NAME_EXIST(2, HttpStatus.BAD_REQUEST, "User name is exist"),
    EMAIL_IN_USE(3, HttpStatus.BAD_REQUEST, "Email id is already in use"),
    VALIDATION_FAILED(4, HttpStatus.BAD_REQUEST, "Validation is failed"),
    UNREGISTERED_USER(5, HttpStatus.NOT_FOUND, "User is not registered"),
    INVALID_USER_NAME_PASSWORD(6, HttpStatus.FORBIDDEN, "Invalid user name and password"),
    TOKEN_EXPIRED(7, HttpStatus.FORBIDDEN, "Token is expired"),
    TOKEN_VERIFICATION_FAILED(8, HttpStatus.FORBIDDEN, "Token verification failed"),
    DATABASE_ERROR(9, HttpStatus.INTERNAL_SERVER_ERROR, "Query failed to execute"),
    ACCESS_DENIED(10, HttpStatus.FORBIDDEN, "Unauthorised access"),
    REQUIRED_PARAMETER_NOT_FOUND(11, HttpStatus.BAD_REQUEST, "required parameter is not found"),
    EMAIL_NOT_FOUND(12, HttpStatus.BAD_REQUEST, "Email not found"),
    INVALID_METHOD_EXCEPTION(13, HttpStatus.METHOD_NOT_ALLOWED, "Invalid method"),

    //contacts apis specific erros
    CONTACTS_NOT_FOUND(14, HttpStatus.NOT_FOUND, "Contacts not found"),
    USER_REGISTRATION_SUCCESS(15, HttpStatus.CREATED, "User registered successfully"),
    CREATE_CONTACT_SUCCESS(16, HttpStatus.CREATED, "Contact has been added"),
    USER_REGISTRATION_FAILED(17, HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed"),
    USER_NOT_FOUND(18, HttpStatus.NOT_FOUND, "User not found"),
    CONTACT_DELETE_SUCCESS(19, HttpStatus.ACCEPTED, "Contact deleted successfully"),
    CONTACT_REGISTRATION_FAILED(20, HttpStatus.INTERNAL_SERVER_ERROR, "Contact registration failed"),
    UPDATE_CONTACT_SUCCESS(21, HttpStatus.ACCEPTED, "Successfully updated contact"),
    UPDATE_CONTACT_FAILED(22, HttpStatus.BAD_REQUEST, "Update contact failed"),
    EXHIBITION_CREATED(23, HttpStatus.CREATED, "Exhibition created successfully"),
    EXHIBITION_UPDATED(24, HttpStatus.ACCEPTED, "Exhibition updated successfully"),
    PAGE_CREATED(25, HttpStatus.CREATED, "Page created successfully"),
    PAGE_UPDATED(26, HttpStatus.ACCEPTED, "Page updated successfully"),
    PROFILE_UPDATED(27, HttpStatus.ACCEPTED, "Profile updated successfully"),
    USER_HISTORY_LOGGED(28, HttpStatus.CREATED, "User history is logged successfully"),

    TRANSACTION_NOT_FOUND(29, HttpStatus.NOT_FOUND, "Transaction not found"),
    EXHIBITION_EXISTS(30, HttpStatus.BAD_REQUEST, "");

    @Getter
    private final int enumCount;

    @Getter
    private final HttpStatus responseCode;

    @Getter
    private final String responseMessage;

    private MessageCode(int enumCount, HttpStatus responseCode, String responseMessage) {
        this.enumCount = enumCount;
        this.responseCode = responseCode;
        this.responseMessage = responseMessage;
    }

    private static final Map<Integer, String> respMessageMap;
    private static final Map<Integer, String> respNameMap;
    static {
        respMessageMap = new HashMap<>();
        respNameMap = new HashMap<>();
        for (MessageCode messageCode : MessageCode.values()) {
            respMessageMap.put(messageCode.enumCount, messageCode.responseMessage);
            respNameMap.put(messageCode.enumCount, messageCode.name());
        }
    }

    public static String findMessage(int i) {
        return respMessageMap.get(i);
    }


    public static String findName(int i) {
        return respMessageMap.get(i);
    }
}
