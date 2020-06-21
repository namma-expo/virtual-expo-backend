package com.nammaexpo.controllers;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.models.enums.SubscriptionPlan;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.payload.response.SubscriptionDetailRespone;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.ExhibitionSubscriptionRepository;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.ExhibitionSubscriptionEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Api(tags = "Exhibition Subscription Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class ExhibitionSubscriptionController {

    @Autowired
    private ExhibitionDetailsRepository exhibitionDetailsRepository;

    @Autowired
    private ExhibitionSubscriptionRepository subscriptionRepository;

    @PostMapping("/subscriptions/{exhibitionId}/{planId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse startSubscription(
            @PathVariable("exhibitionId") String exhibitionId,
            @PathVariable("planId") SubscriptionPlan planId,
            @RequestHeader(value = "Authorization") String authorization) {


        ExhibitionDetailsEntity exhibitionDetails = exhibitionDetailsRepository
                .findByIdentity(exhibitionId)
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));

        Set<ExhibitionSubscriptionEntity> subscriptionEntities = exhibitionDetails.getSubscriptions();

        long count = subscriptionEntities.stream().filter(exhibitionSubscriptionEntity -> exhibitionSubscriptionEntity.getDeletedAt() == null).count();

        log.info("COUNT: {}", count);

        if (count > 0) {
            throw ExpoException.error(MessageCode.INTERNAL_SERVER_ERROR);
        }

        subscriptionRepository.save(ExhibitionSubscriptionEntity.builder()
                .planId(planId)
                .exhibition(exhibitionDetails)
                .createdBy(exhibitionDetails.getExhibitor().getId())
                .build());

        return MessageResponse.builder()
                .message("CREATED")
                .build();
    }

    @PutMapping("/subscriptions/{exhibitionId}/{planId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse upgradeSubscription(
            @PathVariable("exhibitionId") String exhibitionId,
            @PathVariable("planId") SubscriptionPlan planId,
            @RequestHeader(value = "Authorization") String authorization) {

        Supplier<ExpoException> expoExceptionSupplier = () -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND);

        ExhibitionDetailsEntity exhibitionDetails = exhibitionDetailsRepository
                .findByIdentity(exhibitionId)
                .orElseThrow(expoExceptionSupplier);

        Set<ExhibitionSubscriptionEntity> subscriptionEntities = exhibitionDetails.getSubscriptions();

        ExhibitionSubscriptionEntity oldSubscription = subscriptionEntities.stream().filter(exhibitionSubscriptionEntity -> exhibitionSubscriptionEntity.getDeletedAt() == null).findFirst().orElseThrow(expoExceptionSupplier);

        oldSubscription.setDeletedAt(new Date());

        subscriptionRepository.save(oldSubscription);

        subscriptionRepository.save(ExhibitionSubscriptionEntity.builder()
                .planId(planId)
                .exhibition(exhibitionDetails)
                .createdBy(exhibitionDetails.getExhibitor().getId())
                .build());

        return MessageResponse.builder()
                .message("UPDATED")
                .build();
    }

    @GetMapping("/subscriptions/{exhibitionId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public List<SubscriptionDetailRespone> getActiveSubscription(
            @PathVariable("exhibitionId") String exhibitionId,
            @RequestHeader(value = "Authorization") String authorization) {

        ExhibitionDetailsEntity exhibitionDetails = exhibitionDetailsRepository
                .findByIdentity(exhibitionId)
                .orElseThrow(() -> ExpoException.error(MessageCode.TRANSACTION_NOT_FOUND));

        return exhibitionDetails.getSubscriptions().stream()
                .map(exhibitionSubscriptionEntity -> SubscriptionDetailRespone.builder()
                        .exhibitionId(exhibitionDetails.getIdentity())
                        .planId(exhibitionSubscriptionEntity.getPlanId())
                        .createdAt(exhibitionSubscriptionEntity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
