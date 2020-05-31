package com.nammaexpo.controllers;

import com.nammaexpo.constants.VexpoConstants;
import com.nammaexpo.payload.request.SignupRequest;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.RoleRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.EnumRole;
import com.nammaexpo.persistance.model.Role;
import com.nammaexpo.persistance.model.User;
import com.nammaexpo.security.JwtTokenUtil;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import java.util.HashSet;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class RegistrationController {

    private final Logger LOGGER = LoggerFactory.getLogger(this.getClass());

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtTokenUtil jwtUtils;

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        LOGGER.info("Registration Controller has been invoked");

        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(VexpoConstants.ErrorMessage.USER_NAME_EXIST));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse(VexpoConstants.ErrorMessage.EMAIL_IN_USE));
        }

        // Create new user's account
        User user = new User(signUpRequest.getUsername(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()));

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(EnumRole.VISITOR)
                    .orElseThrow(() -> new RuntimeException(VexpoConstants.ErrorMessage.ROLE_NOT_FOUND));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(EnumRole.ADMIN)
                                .orElseThrow(() -> new RuntimeException(VexpoConstants.ErrorMessage.ROLE_NOT_FOUND));
                        roles.add(adminRole);

                        break;
                    case "exhibitor":
                        Role modRole = roleRepository.findByName(EnumRole.EXHIBITOR)
                                .orElseThrow(() -> new RuntimeException(VexpoConstants.ErrorMessage.ROLE_NOT_FOUND));
                        roles.add(modRole);

                        break;
                    case "visitor":
                        Role userRole = roleRepository.findByName(EnumRole.VISITOR)
                                .orElseThrow(() -> new RuntimeException(VexpoConstants.ErrorMessage.ROLE_NOT_FOUND));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse(VexpoConstants.SuccessMessage.USER_REGISTRATION_SUCCESS));
    }
}
