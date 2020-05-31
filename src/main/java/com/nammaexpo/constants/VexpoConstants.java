package com.nammaexpo.constants;

public interface VexpoConstants {

    public interface ErrorMessage {
        String ROLE_NOT_FOUND = "Error: Role is not found";
        String EMAIL_IN_USE = "Error: Email is already in use";
        String USER_NAME_EXIST = "Error: Username is already taken";
        String USER_NOT_FOUND = "Error: User not found with username";

    }

    public interface SuccessMessage {
        String USER_REGISTRATION_SUCCESS = "User registered successfully!";
    }
}
