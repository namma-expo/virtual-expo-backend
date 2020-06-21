package com.nammaexpo.utils;

import com.nammaexpo.models.UserProfile;
import com.nammaexpo.payload.request.SignUpRequest;
import com.nammaexpo.persistance.model.UserEntity;
import com.nammaexpo.persistance.model.UserProfileEntity;

import java.util.UUID;

public interface ModelUtils {

    static UserEntity toUserEntityFromSignUpRequest(SignUpRequest signUpRequest, String password) {

        UserProfileEntity profileEntity = signUpRequest.getProfile() == null ?
                UserProfileEntity.builder().build() : toUserProfileEntity(signUpRequest.getProfile());

        return UserEntity.builder()
                .name(signUpRequest.getName())
                .email(signUpRequest.getEmail())
                .password(password)
                .identity(UUID.randomUUID().toString())
                .role(signUpRequest.getRole())
                .profile(profileEntity)
                .build();
    }

    static UserProfileEntity toUserProfileEntity(UserProfile userProfile) {
        return UserProfileEntity.builder()
                .company(userProfile.getCompany())
                .phoneNumber(userProfile.getPhoneNumber())
                .country(userProfile.getCountry())
                .state(userProfile.getState())
                .city(userProfile.getCity())
                .build();
    }
}
