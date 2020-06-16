package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionContactRepository extends JpaRepository<UserEntity, Integer> {
}