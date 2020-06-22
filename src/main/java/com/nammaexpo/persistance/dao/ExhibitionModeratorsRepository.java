package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.ExhibitionModeratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitionModeratorsRepository extends
        JpaRepository<ExhibitionModeratorEntity, Integer> {

    Optional<ExhibitionModeratorEntity> findByIdAndIsActive(int id, boolean isActive);
}