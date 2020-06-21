package com.nammaexpo.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MessageResponse {

    @ApiModelProperty(notes = "response code")
    private String code;
    @ApiModelProperty(notes = "response message")
    private String message;
    @ApiModelProperty(notes = "detailed message")
    private Map<String, Object> context;
}
