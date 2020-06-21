package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ExhibitionDetailsRepository extends
        JpaRepository<ExhibitionDetailsEntity, Integer> {

    Optional<ExhibitionDetailsEntity> findByIdentity(String exhibitorId);

    Optional<ExhibitionDetailsEntity> findByUrl(String url);

    Optional<ExhibitionDetailsEntity> findByExhibitorId(int exhibitorId);
}