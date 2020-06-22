package com.nammaexpo.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nammaexpo.models.enums.MessageCode;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class MessageResponse {

    @ApiModelProperty(notes = "detailed message")
    private Map<String, Object> context;

    @ApiModelProperty(notes = "response message")
    private MessageCode response;

    @Builder
    public MessageResponse(MessageCode messageCode, Map<String, Object> context) {
        this.response = messageCode;
        this.context = context;
    }
}
