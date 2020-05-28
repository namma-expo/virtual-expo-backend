package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
}
