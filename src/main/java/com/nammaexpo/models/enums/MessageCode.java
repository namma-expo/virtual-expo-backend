package com.nammaexpo.models.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum MessageCode {
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Server error"),
    USER_NAME_EXIST(HttpStatus.BAD_REQUEST, "User name is exist"),
    EMAIL_IN_USE(HttpStatus.BAD_REQUEST, "Email id is already in use"),
    VALIDATION_FAILED(HttpStatus.BAD_REQUEST, "Validation is failed"),
    UNREGISTERED_USER(HttpStatus.NOT_FOUND, "User is not registered"),
    INVALID_USER_NAME_PASSWORD(HttpStatus.FORBIDDEN, "Invalid user name and password"),
    TOKEN_EXPIRED(HttpStatus.FORBIDDEN, "Token is expired"),
    TOKEN_VERIFICATION_FAILED(HttpStatus.FORBIDDEN, "Token verification failed"),
    DATABASE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "Query failed to execute"),
    ACCESS_DENIED(HttpStatus.FORBIDDEN, "Unauthorised access"),
    REQUIRED_PARAMETER_NOT_FOUND(HttpStatus.BAD_REQUEST, "required parameter is not found"),
    EMAIL_NOT_FOUND(HttpStatus.BAD_REQUEST, "Email not found"),
    INVALID_METHOD_EXCEPTION(HttpStatus.METHOD_NOT_ALLOWED, "Invalid method"),

    //contacts apis specific erros
    CONTACTS_NOT_FOUND(HttpStatus.NOT_FOUND, "Contacts not found"),
    USER_REGISTRATION_SUCCESS(HttpStatus.CREATED, "User registered successfully"),
    CREATE_CONTACT_SUCCESS(HttpStatus.CREATED, "Contact has been added"),
    USER_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "User registration failed"),
    USER_NOT_FOUND(HttpStatus.NOT_FOUND, "User not found"),
    CONTACT_DELETE_SUCCESS(HttpStatus.ACCEPTED, "Contact deleted successfully"),
    CONTACT_REGISTRATION_FAILED(HttpStatus.INTERNAL_SERVER_ERROR, "Contact registration failed"),
    UPDATE_CONTACT_SUCCESS(HttpStatus.ACCEPTED, "Successfully updated contact"),
    UPDATE_CONTACT_FAILED(HttpStatus.BAD_REQUEST, "Update contact failed"),
    EXHIBITION_CREATED(HttpStatus.CREATED, "Exhibition created successfully"),
    EXHIBITION_UPDATED(HttpStatus.ACCEPTED, "Exhibition updated successfully"),
    PAGE_CREATED(HttpStatus.CREATED, "Page created successfully"),
    PAGE_UPDATED(HttpStatus.ACCEPTED, "Page updated successfully"),
    PROFILE_UPDATED(HttpStatus.ACCEPTED, "Profile updated successfully"),
    USER_HISTORY_LOGGED(HttpStatus.CREATED, "User history is logged successfully"),

    TRANSACTION_NOT_FOUND(HttpStatus.NOT_FOUND, "Transaction not found"),
    EXHIBITION_EXISTS(HttpStatus.BAD_REQUEST, "Exhibition exists"),
    ACTIVE_SUBSCRIPTION_FOUND(HttpStatus.BAD_REQUEST, "Active Subscription Found"),
    ACTIVE_SUBSCRIPTION_NOT_FOUND(HttpStatus.BAD_REQUEST, "No Active Subscriptions Found"),
    SUBSCRIPTION_ACTIVATED(HttpStatus.CREATED, "Subscription Activated Successfully"),
    SUBSCRIPTION_UPGRADED(HttpStatus.CREATED, "Subscription Upgraded Successfully"),

    EXHIBITOR_ACCESS_DENIED(HttpStatus.NOT_FOUND, "Exhibitor is not active");

    @Getter
    private final String code;

    @Getter
    private final String message;

    @Getter
    private HttpStatus statusCode;

    MessageCode(HttpStatus responseCode, String responseMessage) {
        this.code = this.name();
        this.message = responseMessage;
        this.statusCode = responseCode;
    }
}
