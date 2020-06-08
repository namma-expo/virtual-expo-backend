package com.nammaexpo.controllers;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.expection.ExpoException.ErrorCode;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.SignUpRequest;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.User;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RegistrationController {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    @Autowired
    public RegistrationController(UserRepository userRepository,
        PasswordEncoder encoder) {

        this.userRepository = userRepository;
        this.encoder = encoder;
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(
        @Valid @RequestBody SignUpRequest signUpRequest) {

        log.info("Registration Controller has been invoked");

        if (userRepository.existsByUserName(signUpRequest.getUserName())) {
            throw ExpoException.error(ErrorCode.USER_NAME_EXIST);
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            throw ExpoException.error(ErrorCode.EMAIL_IN_USE);
        }

        // Create new user's account
        User user = new User(signUpRequest.getUserName(),
                encoder.encode(signUpRequest.getPassword()),signUpRequest.getEmail(),
                signUpRequest.getContactNumber(),signUpRequest.getRole());

        userRepository.save(user);

        return ResponseEntity.ok(MessageResponse.builder()
            .messageCode(MessageCode.USER_REGISTRATION_SUCCESS)
            .build());
    }
}
