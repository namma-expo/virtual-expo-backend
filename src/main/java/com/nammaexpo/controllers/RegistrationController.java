package com.nammaexpo.controllers;

import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.models.ExpoUserDetails;
import com.nammaexpo.payload.request.SignUpRequest;
import com.nammaexpo.payload.response.JwtResponse;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.UserEntity;
import com.nammaexpo.utils.JwtUtils;
import com.nammaexpo.utils.ModelUtils;
import com.nammaexpo.utils.SendEmail;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.validation.Valid;
import java.io.IOException;
import java.util.Optional;
import java.util.concurrent.Executors;

@Api(value = "Registration Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RegistrationController {

    private final UserRepository userRepository;

    private final PasswordEncoder encoder;

    private final JwtUtils jwtUtils;

    private final SendEmail sendEmail;

    @Autowired
    public RegistrationController(UserRepository userRepository,
                                  PasswordEncoder encoder, JwtUtils jwtUtils, SendEmail sendEmail) {

        this.userRepository = userRepository;
        this.encoder = encoder;
        this.jwtUtils = jwtUtils;
        this.sendEmail = sendEmail;
    }

    @ApiOperation(value = "User registration", notes = "Both visitor and exhibitor can register through this API and " +
            "on successful registration user will get the access token to login", response = JwtResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User Registration Successful", response = MessageResponse.class),
            @ApiResponse(code = 400, message = "", response = MessageResponse.class)
    })
    @PostMapping("/signup")
    public ResponseEntity<JwtResponse> registerUser(
            @Valid @RequestBody SignUpRequest signUpRequest) {

        log.info("Registration Controller has been invoked");

        Optional<UserEntity> optionalUser = userRepository.findByEmail(signUpRequest.getEmail());

        if (optionalUser.isPresent()) {
            throw ExpoException.error(MessageCode.EMAIL_IN_USE);
        }
        UserEntity userEntity = userRepository.save(ModelUtils
                .toUserEntityFromSignUpRequest(
                        signUpRequest, encoder.encode(signUpRequest.getPassword())));

        final String jwt = jwtUtils.generateJwtToken(new ExpoUserDetails(userEntity));

        Executors.newCachedThreadPool().execute(() -> {
            //Send mail in new thread if jwt token is generated for the user
            if (!StringUtils.isEmpty(jwt)) {
                try {
                    sendEmail.sendEmail(signUpRequest.getEmail(),
                            "Registration Successful", "Template-1");
                } catch (MessagingException|IOException e) {
                    log.error("Error sending email for the user {}", signUpRequest.getEmail(), e);
                }
            }
        });

        return ResponseEntity.ok(new JwtResponse(jwt));
    }
}
