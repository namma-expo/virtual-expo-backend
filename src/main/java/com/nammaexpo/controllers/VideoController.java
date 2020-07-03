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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Mono;

import java.io.IOException;

@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@Api(value = "Video Controller")
public class VideoController {

    @Autowired
    private FileUtils fileUtils;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileMetadataRepository fileMetadataRepository;

    @PostMapping("videos")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse uploadVideoFile(
            @RequestParam("file") MultipartFile file,
            @RequestHeader(value = "Authorization") String authorization) throws IOException {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository
                .findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(MessageCode.USER_NOT_FOUND));

        String fileId = fileUtils.storeVideoFile(file);

        fileMetadataRepository.save(FileMetadataEntity.builder()
                .fileId(fileId)
                .fileName(file.getOriginalFilename())
                .uploadedBy(userEntity.getId())
                .fileSize(file.getSize())
                .contentType(file.getContentType())
                .build());

        return MessageResponse.builder()
                .messageCode(MessageCode.FILE_UPLOAD_SUCCESS)
                .context(ImmutableMap.of(
                        "fileId", fileId,
                        "fileSize", file.getSize()))
                .build();
    }

    @GetMapping("videos/download/{fileId}")
    public ResponseEntity downlaodVideo(
            @PathVariable("fileId") String fileId,
            @RequestHeader(value = "Authorization") String authorization
    ) {

        Resource resource = fileUtils.fetchVideoForDownload(fileId);

        FileMetadataEntity metadata = fileMetadataRepository.findByFileId(fileId)
                .orElseThrow(() -> ExpoException.error(MessageCode.FILE_NOT_FOUND));

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + metadata.getFileName())
                .body(resource);
    }

    @GetMapping("videos/{fileId}")
    public Mono<ResponseEntity<byte[]>> getVideoFile(@RequestHeader(value = "Range", required = false) String range,
                                                     @PathVariable("fileId") String fileId,
                                                     @RequestHeader(value = "Authorization") String authorization) {

        FileMetadataEntity metadata = fileMetadataRepository.findByFileId(fileId)
                .orElseThrow(() -> ExpoException.error(MessageCode.FILE_NOT_FOUND));

        long start = getStartRange(range);
        long end = getEndRange(range, metadata.getFileSize());

        byte[] data = fileUtils.fetchVideoFile(fileId, start, end);

        return Mono.just(ResponseEntity
                .status(HttpStatus.PARTIAL_CONTENT)
                .header("Content-Type", metadata.getContentType())
                .header("Accept-Ranges", "bytes")
                .header("Content-Length", String.valueOf((end - start) + 1))
                .header("Content-Range", "bytes " + start + "-" + end + "/" + metadata.getFileSize())
                .body(data));
    }

    private long getStartRange(String range) {
        long start = 0;
        if (range != null) {
            String[] ranges = range.split("-");
            start = Long.parseLong(ranges[0].substring(6));
        }
        return start;
    }

    private long getEndRange(String range, long fileSize) {
        long end = fileSize - 1;

        if (range != null) {
            String[] ranges = range.split("-");

            end = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileSize - 1;

            if (fileSize < end) {
                end = fileSize - 1;
            }
        }
        return end;
    }
}
