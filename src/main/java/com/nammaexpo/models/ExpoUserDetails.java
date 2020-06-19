package com.nammaexpo.models;

import com.nammaexpo.models.enums.Role;
import com.nammaexpo.persistance.model.UserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class ExpoUserDetails implements UserDetails {

    private final String userName;
    private final String password;
    private final Role role;
    private final String identity;
    private final List<GrantedAuthority> authorities;


    public ExpoUserDetails(UserEntity userEntity) {
        this.userName = userEntity.getEmail();
        this.password = userEntity.getPassword();
        this.role = userEntity.getRole();
        this.identity = userEntity.getIdentity();
        this.authorities = Collections
                .singletonList(new SimpleGrantedAuthority(userEntity.getRole().name()));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return userName;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }


    public Role getRole() {
        return role;
    }

    public String getIdentity() {
        return identity;
    }
}
