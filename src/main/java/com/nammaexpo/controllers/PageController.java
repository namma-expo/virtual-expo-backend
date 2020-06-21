package com.nammaexpo.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.models.layout.Layout;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.PageRepository;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.PageEntity;
import com.nammaexpo.utils.SerDe;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;

@Api(tags = "Exhibition Page Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class PageController {

    @Autowired
    private PageRepository pageRepository;

    @Autowired
    private ExhibitionDetailsRepository exhibitionDetailsRepository;

    @PostMapping("/pages/{exhibitionId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse createPage(
            @PathVariable("exhibitionId") String exhibitionId,
            @NotNull @RequestBody Layout layout,
            @RequestHeader(value = "Authorization") String authorization
    ) throws Exception {

        ExhibitionDetailsEntity exhibitionDetailsEntity = exhibitionDetailsRepository
                .findByIdentity(exhibitionId)
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));

        pageRepository.save(PageEntity.builder()
                .isActive(true)
                .content(SerDe.mapper().writeValueAsBytes(layout))
                .exhibitionDetails(exhibitionDetailsEntity)
                .createdBy(exhibitionDetailsEntity.getExhibitor().getId())
                .build());

        return MessageResponse.builder()
                .code(MessageCode.findName(25))
                .message(MessageCode.findMessage(25))
                .build();
    }

    @GetMapping("/pages/{exhibitionId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public Layout getPage(
            @PathVariable("exhibitionId") String exhibitionId,
            @RequestHeader(value = "Authorization") String authorization
    ) {

        return exhibitionDetailsRepository.findByIdentity(exhibitionId)
                .map(ExhibitionDetailsEntity::getPageDetails)
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));
    }

    @PutMapping("/pages/{exhibitionId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse updatePage(
            @PathVariable("exhibitionId") String exhibitionId,
            @NotNull @RequestBody Layout layout,
            @RequestHeader(value = "Authorization") String authorization
    ) throws JsonProcessingException {

        ExhibitionDetailsEntity exhibitionDetailsEntity = exhibitionDetailsRepository
                .findByIdentity(exhibitionId)
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));

        PageEntity pageEntity = exhibitionDetailsEntity.getPage();

        pageEntity.setContent(SerDe.mapper().writeValueAsBytes(layout));

        pageRepository.save(pageEntity);

        return MessageResponse.builder()
                .code(MessageCode.findName(26))
                .message(MessageCode.findMessage(26))
                .build();
    }
}
