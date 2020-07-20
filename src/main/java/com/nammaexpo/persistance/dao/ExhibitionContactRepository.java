package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.ExhibitionContactEntity;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExhibitionContactRepository extends JpaRepository<ExhibitionContactEntity, Integer> {

    Optional<ExhibitionContactEntity> findByEmail(String email);

    List<ExhibitionContactEntity> findAllByExhibitionDetails(ExhibitionDetailsEntity exhibitionDetails);

    Optional<ExhibitionContactEntity> findByEmailAndExhibitionDetails(
            String email, ExhibitionDetailsEntity exhibitionDetails);
}