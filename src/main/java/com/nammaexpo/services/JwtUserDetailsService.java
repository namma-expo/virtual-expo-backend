package com.nammaexpo.services;

import com.nammaexpo.constants.VexpoConstants;
import com.nammaexpo.persistance.dao.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class JwtUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {

        com.nammaexpo.persistance.model.User user = userRepository.findByUserName(userName);
                //.orElseThrow(() -> new UsernameNotFoundException(VexpoConstants.ErrorMessage.USER_NOT_FOUND));
        return new User(user.getUserName(), user.getPassword(),
                new ArrayList<>());
    }
}