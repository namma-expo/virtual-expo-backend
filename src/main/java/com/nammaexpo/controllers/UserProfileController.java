package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.UserProfile;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.UserProfileRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.UserEntity;
import com.nammaexpo.persistance.model.UserProfileEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Api(tags = "User UserProfile Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserProfileRepository userProfileRepository;

    @GetMapping("/users/profile")
    public UserProfile getUserProfile(
            @RequestHeader(value = "Authorization") String authorization
    ) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        ErrorCode.TRANSACTION_NOT_FOUND)
                );

        UserProfileEntity userProfileEntity = userEntity.getProfile();

        return UserProfile.builder()
                .company(userProfileEntity.getCompany())
                .city(userProfileEntity.getCity())
                .country(userProfileEntity.getCountry())
                .phoneNumber(userProfileEntity.getPhoneNumber())
                .state(userProfileEntity.getState())
                .build();
    }


    @PutMapping("/users/profile")
    public MessageResponse updateUserProfile(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody @NotNull UserProfile userProfile
    ) {
        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        ErrorCode.TRANSACTION_NOT_FOUND)
                );

        UserProfileEntity userProfileEntity = userEntity.getProfile();

        userProfileEntity.setCity(userProfile.getCity());
        userProfileEntity.setCompany(userProfile.getCompany());
        userProfileEntity.setCountry(userProfile.getCountry());
        userProfileEntity.setPhoneNumber(userProfile.getPhoneNumber());
        userProfileEntity.setState(userProfile.getState());

        userProfileRepository.save(userProfileEntity);

        return MessageResponse.builder()
                .messageCode(MessageCode.PROFILE_UPDATED)
                .build();
    }
}
