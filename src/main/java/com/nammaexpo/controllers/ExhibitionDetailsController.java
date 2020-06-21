package com.nammaexpo.controllers;

import com.google.common.collect.ImmutableMap;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ExhibitionRequest;
import com.nammaexpo.payload.response.ExhibitionDetailResponse;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.UserEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Api(tags = "Exhibition Details Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ExhibitionDetailsController {

    @Autowired
    private ExhibitionDetailsRepository exhibitionDetailsRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping(
            value = "/exhibitions",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse createExhibition(
            @RequestHeader(value = "Authorization") String authorization,
            @RequestBody @NotNull ExhibitionRequest exhibitionRequest) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        MessageCode.TRANSACTION_NOT_FOUND)
                );

        Optional<ExhibitionDetailsEntity> entity = exhibitionDetailsRepository
                .findByUrl(exhibitionRequest.getUrl());

        if (entity.isPresent()) {
            throw ExpoException.error(MessageCode.EXHIBITION_EXISTS);
        }

        ExhibitionDetailsEntity exhibitionDetailsEntity = ExhibitionDetailsEntity.builder()
                .exhibitor(userEntity)
                .identity(UUID.randomUUID().toString())
                .logo(exhibitionRequest.getLogo())
                .name(exhibitionRequest.getName())
                .url(exhibitionRequest.getUrl())
                .build();

        exhibitionDetailsRepository.save(exhibitionDetailsEntity);

        return MessageResponse.builder()
                .context(ImmutableMap.of("identity", exhibitionDetailsEntity.getIdentity()))
                .messageCode(MessageCode.EXHIBITION_CREATED)
                .build();
    }

    @PutMapping(value = "/exhibitions/{exhibitionId}",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse updateExhibition(
            @RequestHeader(value = "Authorization") String authorization,
            @PathVariable("exhibitionId") String exhibitionId,
            @RequestBody @NotNull ExhibitionRequest exhibitionRequest) {

        ExhibitionDetailsEntity exhibitionDetailsEntity = exhibitionDetailsRepository
                .findByIdentity(exhibitionId)
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));

        exhibitionDetailsEntity.setName(exhibitionRequest.getName());
        exhibitionDetailsEntity.setLogo(exhibitionRequest.getLogo());
        exhibitionDetailsEntity.setUrl(exhibitionRequest.getUrl());

        exhibitionDetailsRepository.save(exhibitionDetailsEntity);

        return MessageResponse.builder()
                .messageCode(MessageCode.EXHIBITION_UPDATED)
                .build();
    }

    @GetMapping(value = "/exhibitions/all", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<ExhibitionDetailResponse> getAllExhibitions(
            @RequestHeader(value = "Authorization") String authorization) {

        List<ExhibitionDetailsEntity> exhibitionDetails = exhibitionDetailsRepository.findAll();

        return exhibitionDetails.stream()
                .map(exhibitionDetailsEntity -> ExhibitionDetailResponse.builder()
                        .identity(exhibitionDetailsEntity.getIdentity())
                        .name(exhibitionDetailsEntity.getName())
                        .logo(exhibitionDetailsEntity.getLogo())
                        .url(exhibitionDetailsEntity.getUrl())
                        .build())
                .collect(Collectors.toList());
    }

    @GetMapping(value = "/exhibitions/{exhibitionId}",
            produces = MediaType.APPLICATION_JSON_VALUE)
    public ExhibitionDetailResponse getExhibition(
            @RequestHeader(value = "Authorization") String authorization,
            @PathVariable("exhibitionId") String exhibitionId) {

        return exhibitionDetailsRepository.findByIdentity(exhibitionId)
                .map(exhibitionDetailsEntity -> ExhibitionDetailResponse.builder()
                        .url(exhibitionDetailsEntity.getUrl())
                        .identity(exhibitionDetailsEntity.getIdentity())
                        .name(exhibitionDetailsEntity.getName())
                        .logo(exhibitionDetailsEntity.getLogo())
                        .layout(exhibitionDetailsEntity.getPageDetails())
                        .build())
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));
    }


    @GetMapping(value = "/exhibitions", produces = MediaType.APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public ExhibitionDetailResponse getExhibitionBasedOnExhibitor(
            @RequestHeader(value = "Authorization") String authorization) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Supplier<ExpoException> expoExceptionSupplier = () -> ExpoException
                .error(MessageCode.TRANSACTION_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(userName).orElseThrow(
                expoExceptionSupplier
        );

        return exhibitionDetailsRepository.findByExhibitorId(userEntity.getId())
                .map(exhibitionDetailsEntity -> ExhibitionDetailResponse.builder()
                        .identity(exhibitionDetailsEntity.getIdentity())
                        .name(exhibitionDetailsEntity.getName())
                        .logo(exhibitionDetailsEntity.getLogo())
                        .layout(exhibitionDetailsEntity.getPageDetails())
                        .build())
                .orElseThrow(expoExceptionSupplier);
    }
}
