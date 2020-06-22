package com.nammaexpo.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nammaexpo.models.enums.SubscriptionPlan;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SubscriptionDetailResponse {
    private SubscriptionPlan planId;
    private String exhibitionId;
    private Date createdAt;
}
