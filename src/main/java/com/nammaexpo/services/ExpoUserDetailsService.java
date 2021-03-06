package com.nammaexpo.services;

import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.ExpoUserDetails;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ExpoUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public ExpoUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public ExpoUserDetails loadUserByUsername(String email) {

        Optional<UserEntity> userOptional = userRepository.findByEmail(email);

        return userOptional
                .map(ExpoUserDetails::new)
                .orElseThrow(() -> ExpoException.error(MessageCode.UNREGISTERED_USER)
                );
    }
}
