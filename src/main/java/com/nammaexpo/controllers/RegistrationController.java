package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.ErrorResponse;
import com.nammaexpo.models.ExpoUserDetails;
import com.nammaexpo.payload.request.SignUpRequest;
import com.nammaexpo.payload.response.JwtResponse;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.UserEntity;
import com.nammaexpo.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import java.util.Optional;
import java.util.UUID;
import javax.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Api(value = "Registration Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class RegistrationController {

  private final UserRepository userRepository;

  private final PasswordEncoder encoder;

  private JwtUtils jwtUtils;

  @Autowired
  public RegistrationController(UserRepository userRepository,
      PasswordEncoder encoder, JwtUtils jwtUtils) {

    this.userRepository = userRepository;
    this.encoder = encoder;
    this.jwtUtils = jwtUtils;
  }

  @ApiOperation(value = "Registration")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "User Registration Successful", response = MessageResponse.class),
      @ApiResponse(code = 400, message = "User Registration Failed", response = ErrorResponse.class)
  })
  @PostMapping("/signup")
  public ResponseEntity<JwtResponse> registerUser(
      @Valid @RequestBody SignUpRequest signUpRequest) {

    log.info("Registration Controller has been invoked");

    Optional<UserEntity> optionalUser = userRepository.findByEmail(signUpRequest.getEmail());

    if (optionalUser.isPresent()) {
      throw ExpoException.error(ErrorCode.EMAIL_IN_USE);
    }

    UserEntity userEntity = userRepository.save(UserEntity.builder()
        .name(signUpRequest.getName())
        .email(signUpRequest.getEmail())
        .password(encoder.encode(signUpRequest.getPassword()))
        .identity(UUID.randomUUID().toString())
        .role(signUpRequest.getRole())
        .build());

    final String jwt = jwtUtils.generateJwtToken(new ExpoUserDetails(userEntity));

    return ResponseEntity.ok(new JwtResponse(jwt));
  }
}
