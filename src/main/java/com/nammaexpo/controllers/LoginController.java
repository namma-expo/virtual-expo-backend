package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.ErrorResponse;
import com.nammaexpo.models.ExpoUserDetails;
import com.nammaexpo.payload.request.LoginRequest;
import com.nammaexpo.payload.response.JwtResponse;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.services.ExpoUserDetailsService;
import com.nammaexpo.utils.JwtUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@Api(tags = "Login Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class LoginController {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtUtils jwtTokenUtils;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private ExpoUserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder encoder;

  @ApiOperation(value = "Login User")
  @ApiResponses(value = {
      @ApiResponse(code = 200, message = "Authenticated", response = JwtResponse.class),
      @ApiResponse(code = 403, message = "Authentication Failed", response = ErrorResponse.class)
  })
  @PostMapping("/authenticate")
  public ResponseEntity<JwtResponse> authenticateUser(
      @RequestBody LoginRequest loginRequest) {

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequest.getUserName(),
              loginRequest.getPassword())
      );

    } catch (BadCredentialsException e) {
      throw ExpoException.error(ErrorCode.INVALID_USER_NAME_PASSWORD);
    }

    final ExpoUserDetails userDetails = userDetailsService
        .loadUserByUsername(loginRequest.getUserName());

    final String jwt = jwtTokenUtils.generateJwtToken(userDetails);

    return ResponseEntity.ok(new JwtResponse(jwt));
  }
}