package com.nammaexpo.controllers;

import com.nammaexpo.expection.ErrorCode;
import com.nammaexpo.expection.ExpoException;
import com.nammaexpo.models.enums.MessageCode;
import com.nammaexpo.models.enums.UserAction;
import com.nammaexpo.payload.response.MessageResponse;
import com.nammaexpo.payload.response.UserHistoryResponse;
import com.nammaexpo.persistance.dao.UserHistoryRepository;
import com.nammaexpo.persistance.dao.UserRepository;
import com.nammaexpo.persistance.model.UserEntity;
import com.nammaexpo.persistance.model.UserHistoryEntity;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Api(tags = "User History Controller")
@Slf4j
@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
public class UserHistoryController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserHistoryRepository userHistoryRepository;

    @PostMapping("/user/history/{action}/{exhibitionId}")
    public MessageResponse createUserHistory(
            @PathVariable("action") UserAction userAction,
            @PathVariable("exhibitionId") int exhibitionId,
            @RequestHeader(value = "Authorization") String authorization
    ) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        ErrorCode.TRANSACTION_NOT_FOUND)
                );

        userHistoryRepository.save(UserHistoryEntity.builder()
                .action(userAction)
                .userId(userEntity.getId())
                .exhibitionId(exhibitionId)
                .build());

        return MessageResponse.builder()
                .messageCode(MessageCode.USER_HISTORY_LOGGED)
                .build();
    }


    @GetMapping("/user/history")
    public List<UserHistoryResponse> getUserHistory(
            @RequestHeader(value = "Authorization") String authorization
    ) {

        String userName = SecurityContextHolder.getContext().getAuthentication().getName();

        UserEntity userEntity = userRepository.findByEmail(userName)
                .orElseThrow(() -> ExpoException.error(
                        ErrorCode.TRANSACTION_NOT_FOUND)
                );

        List<UserHistoryEntity> userHistoryEntities = userHistoryRepository.findByUserId(userEntity.getId());

        return userHistoryEntities.stream()
                .map(userHistoryEntity -> UserHistoryResponse.builder()
                        .date(userHistoryEntity.getCreatedAt())
                        .exhibitionId(userHistoryEntity.getExhibitionId())
                        .userAction(userHistoryEntity.getAction())
                        .build())
                .collect(Collectors.toList());


    }
}
