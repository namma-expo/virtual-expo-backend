package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.ExhibitionModeratorEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionModeratorsRepository extends JpaRepository<ExhibitionModeratorEntity, Integer> {
}