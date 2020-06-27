package com.nammaexpo.persistance.dao;

import com.nammaexpo.persistance.model.FileMetadataEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadataEntity, Integer> {

    Optional<FileMetadataEntity> findByFileId(String fileId);

}