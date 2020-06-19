package com.nammaexpo.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nammaexpo.models.enums.MessageCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {
    @ApiModelProperty(notes = "response code")
    private MessageCode messageCode;
    @ApiModelProperty(notes = "response message")
    private String message;
}
