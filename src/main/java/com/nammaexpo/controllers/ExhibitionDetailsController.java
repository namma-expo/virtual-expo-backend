package com.nammaexpo.controllers;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.expection.ExpoException.ErrorCode;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.payload.request.ExhibitionRequest;
import com.nammaexpo.payload.response.ExhibitionDetailResponse;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.UserEntity;
import io.swagger.annotations.Api;
import java.util.List;
import java.util.UUID;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

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
      value = "/exhibition",
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
            ErrorCode.TRANSACTION_NOT_FOUND)
        );

    ExhibitionDetailsEntity exhibitionDetailsEntity = ExhibitionDetailsEntity.builder()
        .exhibitor(userEntity)
        .identity(UUID.randomUUID().toString())
        .logo(exhibitionRequest.getLogo())
        .name(exhibitionRequest.getName())
        .build();

    exhibitionDetailsRepository.save(exhibitionDetailsEntity);

    return MessageResponse.builder()
        .messageCode(MessageCode.EXHIBITION_CREATED)
        .build();
  }

  @PutMapping(value = "/exhibition/{exhibitionId}",
      produces = MediaType.APPLICATION_JSON_VALUE,
      consumes = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('EXHIBITOR')")
  public MessageResponse updateExhibition(
      @RequestHeader(value = "Authorization") String authorization,
      @PathVariable("exhibitionId") int exhibitionId,
      @RequestBody @NotNull ExhibitionRequest exhibitionRequest) {

    ExhibitionDetailsEntity exhibitionDetailsEntity = exhibitionDetailsRepository
        .findById(exhibitionId)
        .orElseThrow(() -> ExpoException.error(ErrorCode.TRANSACTION_NOT_FOUND));

    exhibitionDetailsEntity.setName(exhibitionRequest.getName());
    exhibitionDetailsEntity.setLogo(exhibitionRequest.getLogo());
    exhibitionDetailsRepository.save(exhibitionDetailsEntity);

    return MessageResponse.builder()
        .messageCode(MessageCode.EXHIBITION_UPDATED)
        .build();
  }

  @GetMapping(value = "/exhibitions", produces = MediaType.APPLICATION_JSON_VALUE)
  public List<ExhibitionDetailResponse> getAllExhibitions(
      @RequestHeader(value = "Authorization") String authorization) {

    List<ExhibitionDetailsEntity> exhibitionDetails = exhibitionDetailsRepository.findAll();

    return exhibitionDetails.stream()
        .map(exhibitionDetailsEntity -> ExhibitionDetailResponse.builder()
            .exhibitionId(exhibitionDetailsEntity.getId())
            .identifier(exhibitionDetailsEntity.getIdentity())
            .name(exhibitionDetailsEntity.getName())
            .logo(exhibitionDetailsEntity.getLogo())
            .build())
        .collect(Collectors.toList());
  }

  @GetMapping(value = "/exhibition/{exhibitionId}",
      produces = MediaType.APPLICATION_JSON_VALUE)
  public ExhibitionDetailResponse getExhibition(
      @RequestHeader(value = "Authorization") String authorization,
      @PathVariable("exhibitionId") int exhibitionId) {

    return exhibitionDetailsRepository.findById(exhibitionId)
        .map(exhibitionDetailsEntity -> ExhibitionDetailResponse.builder()
            .exhibitionId(exhibitionDetailsEntity.getId())
            .identifier(exhibitionDetailsEntity.getIdentity())
            .name(exhibitionDetailsEntity.getName())
            .logo(exhibitionDetailsEntity.getLogo())
            .layout(exhibitionDetailsEntity.getPageDetails())
            .build())
        .orElseThrow(() -> ExpoException.error(ErrorCode.TRANSACTION_NOT_FOUND));
  }


  @GetMapping(value = "/exhibition", produces = MediaType.APPLICATION_JSON_VALUE)
  @PreAuthorize("hasAuthority('EXHIBITOR')")
  public ExhibitionDetailResponse getExhibitionBasedOnExhibitor(
      @RequestHeader(value = "Authorization") String authorization) {

    String userName = SecurityContextHolder.getContext().getAuthentication().getName();

    Supplier<ExpoException> expoExceptionSupplier = () -> ExpoException
        .error(ErrorCode.TRANSACTION_NOT_FOUND);

    UserEntity userEntity = userRepository.findByEmail(userName).orElseThrow(
        expoExceptionSupplier
    );

    return exhibitionDetailsRepository.findByExhibitorId(userEntity.getId())
        .map(exhibitionDetailsEntity -> ExhibitionDetailResponse.builder()
            .exhibitionId(exhibitionDetailsEntity.getId())
            .identifier(exhibitionDetailsEntity.getIdentity())
            .name(exhibitionDetailsEntity.getName())
            .logo(exhibitionDetailsEntity.getLogo())
            .layout(exhibitionDetailsEntity.getPageDetails())
            .build())
        .orElseThrow(expoExceptionSupplier);
  }
}
