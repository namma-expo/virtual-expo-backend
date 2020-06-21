package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.UserHistoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserHistoryRepository extends JpaRepository<UserHistoryEntity, Integer> {

}