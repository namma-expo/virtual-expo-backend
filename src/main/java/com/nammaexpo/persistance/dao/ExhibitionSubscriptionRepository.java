package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.ExhibitionSubscriptionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionSubscriptionRepository extends
        JpaRepository<ExhibitionSubscriptionEntity, Integer> {

}