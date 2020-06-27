package com.nammaexpo.controllers;

import com.google.common.collect.ImmutableMap;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.FileMetadataRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.FileMetadataEntity;
import com.nammaexpo.persistance.model.UserEntity;
import com.nammaexpo.utils.FileUtils;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Image Controller")
public class ImageController {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @PostMapping("images")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse uploadImageFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "Authorization") String authorization) throws IOException {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository
                .findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(MessageCode.USER_NOT_FOUND));

        String fileId = fileUtils.storeImageFile(file);

        fileMetadataRepository.save(FileMetadataEntity.builder()
                .fileId(fileId)
                .fileName(file.getOriginalFilename())
                .uploadedBy(userEntity.getId())
                .fileSize(file.getSize())
                .build());

        return MessageResponse.builder()
                .messageCode(MessageCode.FILE_UPLOAD_SUCCESS)
                .context(ImmutableMap.of(
                        "fileId", fileId,
                        "fileSize", file.getSize()))
                .build();
    }

    @GetMapping("images/{fileId}")
    public ResponseEntity getImageFile(
            @PathVariable("fileId") String fileId,
            @RequestHeader(value = "Authorization") String authorization
    ) {

        Resource resource = fileUtils.fetchImageFile(fileId);

        FileMetadataEntity metadata = fileMetadataRepository.findByFileId(fileId)
                .orElseThrow(() -> ExpoException.error(MessageCode.FILE_NOT_FOUND));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + metadata.getFileName())
                .body(resource);
    }
}
