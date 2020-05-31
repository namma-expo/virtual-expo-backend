package com.nammaexpo.services;

import com.nammaexpo.constants.VexpoConstants;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(VexpoConstants.ErrorMessage.USER_NOT_FOUND));

        return JwtUserDetailsImpl.build(user);
    }
}