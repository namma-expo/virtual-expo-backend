package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
