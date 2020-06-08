package com.nammaexpo.controllers;

import com.nammaexpo.payload.request.LoginRequest;
import com.nammaexpo.payload.response.JwtResponse;
import com.nammaexpo.security.JwtTokenUtil;
import com.nammaexpo.services.JwtUserDetailsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class LoginController {

  private final AuthenticationManager authenticationManager;

  private final JwtTokenUtil jwtTokenUtil;

  private final JwtUserDetailsService userDetailsService;

  @Autowired
  public LoginController(
      AuthenticationManager authenticationManager,
      JwtTokenUtil jwtTokenUtil,
      JwtUserDetailsService userDetailsService) {

    this.authenticationManager = authenticationManager;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userDetailsService = userDetailsService;
  }

  @PostMapping(value = "/authenticate")
  public ResponseEntity<JwtResponse> createAuthenticationToken(
      @RequestBody LoginRequest authenticationRequest) throws Exception {

    log.info("Login Controller has been invoked");

    try {
      authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(authenticationRequest.getUserName(),
              authenticationRequest.getPassword())
      );
    } catch (BadCredentialsException e) {
      throw new Exception("Incorrect username or password", e);
    }

    final UserDetails userDetails = userDetailsService
        .loadUserByUsername(authenticationRequest.getUserName());

    final String jwt = jwtTokenUtil.generateToken(userDetails);

    return ResponseEntity.ok(JwtResponse.builder()
        .token(jwt)
        .build());
  }

    /*private void authenticate(String username, String password) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }*/
}