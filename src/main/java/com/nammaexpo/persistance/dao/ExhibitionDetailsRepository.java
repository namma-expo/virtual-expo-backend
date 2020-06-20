package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExhibitionDetailsRepository extends
    JpaRepository<ExhibitionDetailsEntity, Integer> {

  Optional<ExhibitionDetailsEntity> findByExhibitorId(int exhibitorId);
}