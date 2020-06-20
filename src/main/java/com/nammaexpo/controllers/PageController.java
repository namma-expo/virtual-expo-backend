package com.nammaexpo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.expection.ExpoException.ErrorCode;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.models.layout.Layout;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.PageRepository;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.PageEntity;
import io.swagger.annotations.Api;
import javax.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@Api(tags = "Exhibition Page Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PageController {

  @Autowired
  private PageRepository pageRepository;

  @Autowired
  private ExhibitionDetailsRepository exhibitionDetailsRepository;

  @PostMapping("/page/{exhibitionId}")
  @PreAuthorize("hasAuthority('EXHIBITOR')")
  public MessageResponse createPage(
      @PathVariable("exhibitionId") int exhibitionId,
      @NotNull @RequestBody Layout layout,
      @RequestHeader(value = "Authorization") String authorization
  ) throws Exception {

    ExhibitionDetailsEntity exhibitionDetailsEntity = exhibitionDetailsRepository
        .findById(exhibitionId)
        .orElseThrow(() -> ExpoException.error(ErrorCode.TRANSACTION_NOT_FOUND));

    ObjectMapper mapper = new ObjectMapper();

    pageRepository.save(PageEntity.builder()
        .isActive(true)
        .content(mapper.writeValueAsBytes(layout))
        .exhibitionDetails(exhibitionDetailsEntity)
        .createdBy(exhibitionDetailsEntity.getExhibitor().getId())
        .build());

    return MessageResponse.builder()
        .messageCode(MessageCode.PAGE_CREATED)
        .build();
  }

  @GetMapping("/page/{exhibitionId}")
  @PreAuthorize("hasAuthority('EXHIBITOR')")
  public Layout getPage(
      @PathVariable("exhibitionId") int exhibitionId,
      @RequestHeader(value = "Authorization") String authorization
  ) {

    return exhibitionDetailsRepository.findById(exhibitionId)
        .map(ExhibitionDetailsEntity::getPageDetails)
        .orElseThrow(() -> ExpoException.error(ErrorCode.TRANSACTION_NOT_FOUND));
  }

  @PutMapping("/page/{exhibitionId}")
  @PreAuthorize("hasAuthority('EXHIBITOR')")
  public MessageResponse updatePage(
      @PathVariable("exhibitionId") int exhibitionId,
      @NotNull @RequestBody Layout layout,
      @RequestHeader(value = "Authorization") String authorization
  ) throws JsonProcessingException {

    ExhibitionDetailsEntity exhibitionDetailsEntity = exhibitionDetailsRepository
        .findById(exhibitionId)
        .orElseThrow(() -> ExpoException.error(ErrorCode.TRANSACTION_NOT_FOUND));

    ObjectMapper mapper = new ObjectMapper();
    PageEntity pageEntity = exhibitionDetailsEntity.getPage();

    pageEntity.setContent(mapper.writeValueAsBytes(layout));

    pageRepository.save(pageEntity);

    return MessageResponse.builder()
        .messageCode(MessageCode.PAGE_UPDATED)
        .build();
  }
}
