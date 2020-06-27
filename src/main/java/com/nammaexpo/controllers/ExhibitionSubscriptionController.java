package com.nammaexpo.controllers;

import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.models.enums.SubscriptionPlan;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.payload.response.SubscriptionDetailResponse;
import com.nammaexpo.persistance.dao.ExhibitionDetailsRepository;
import com.nammaexpo.persistance.dao.ExhibitionSubscriptionRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.ExhibitionDetailsEntity;
import com.nammaexpo.persistance.model.ExhibitionSubscriptionEntity;
import com.nammaexpo.persistance.model.UserEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
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
    private UserRepository userRepository;

    @Autowired
    private ExhibitionSubscriptionRepository subscriptionRepository;

    @PostMapping("/subscriptions/{planId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse startSubscription(
            @PathVariable("planId") SubscriptionPlan planId,
            @RequestHeader(value = "Authorization") String authorization) {


        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Supplier<ExpoException> expoExceptionSupplier = () -> ExpoException.error(
                MessageCode.TRANSACTION_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(expoExceptionSupplier
                );

        ExhibitionDetailsEntity exhibitionDetails = exhibitionDetailsRepository
                .findByExhibitorId(userEntity.getId())
                .orElseThrow(expoExceptionSupplier);

        Set<ExhibitionSubscriptionEntity> subscriptionEntities = exhibitionDetails.getSubscriptions();

        long count = subscriptionEntities.stream()
                .filter(subscription -> subscription.getDeletedAt() == null)
                .count();

        if (count > 0) {
            throw ExpoException.error(MessageCode.ACTIVE_SUBSCRIPTION_FOUND);
        }

        subscriptionRepository.save(ExhibitionSubscriptionEntity.builder()
                .planId(planId)
                .exhibition(exhibitionDetails)
                .createdBy(exhibitionDetails.getExhibitor().getId())
                .build());

        return MessageResponse.builder()
                .messageCode(MessageCode.SUBSCRIPTION_ACTIVATED)
                .build();
    }

    @PutMapping("/subscriptions/{planId}")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public MessageResponse upgradeSubscription(
            @PathVariable("planId") SubscriptionPlan planId,
            @RequestHeader(value = "Authorization") String authorization) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Supplier<ExpoException> expoExceptionSupplier = () -> ExpoException.error(
                MessageCode.TRANSACTION_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(expoExceptionSupplier
                );

        ExhibitionDetailsEntity exhibitionDetails = exhibitionDetailsRepository
                .findByExhibitorId(userEntity.getId())
                .orElseThrow(expoExceptionSupplier);

        Set<ExhibitionSubscriptionEntity> subscriptionEntities = exhibitionDetails.getSubscriptions();

        ExhibitionSubscriptionEntity oldSubscription = subscriptionEntities.stream()
                .filter(subscription -> subscription.getDeletedAt() == null)
                .findFirst()
                .orElseThrow(() -> ExpoException.error(MessageCode.ACTIVE_SUBSCRIPTION_NOT_FOUND));

        oldSubscription.setDeletedAt(new Date());

        subscriptionRepository.save(oldSubscription);

        subscriptionRepository.save(ExhibitionSubscriptionEntity.builder()
                .planId(planId)
                .exhibition(exhibitionDetails)
                .createdBy(exhibitionDetails.getExhibitor().getId())
                .build());

        return MessageResponse.builder()
                .messageCode(MessageCode.SUBSCRIPTION_UPGRADED)
                .build();
    }

    @GetMapping("/subscriptions")
    @PreAuthorize("hasAuthority('EXHIBITOR')")
    public List<SubscriptionDetailResponse> getActiveSubscription(
            @RequestHeader(value = "Authorization") String authorization) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        Supplier<ExpoException> expoExceptionSupplier = () -> ExpoException.error(
                MessageCode.TRANSACTION_NOT_FOUND);

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(expoExceptionSupplier
                );

        ExhibitionDetailsEntity exhibitionDetails = exhibitionDetailsRepository
                .findByExhibitorId(userEntity.getId())
                .orElseThrow(expoExceptionSupplier);

        return exhibitionDetails.getSubscriptions().stream()
                .filter(subscription -> subscription.getDeletedAt() == null)
                .map(exhibitionSubscriptionEntity -> SubscriptionDetailResponse.builder()
                        .exhibitionId(exhibitionDetails.getIdentity())
                        .planId(exhibitionSubscriptionEntity.getPlanId())
                        .createdAt(exhibitionSubscriptionEntity.getCreatedAt())
                        .build())
                .collect(Collectors.toList());
    }
}
