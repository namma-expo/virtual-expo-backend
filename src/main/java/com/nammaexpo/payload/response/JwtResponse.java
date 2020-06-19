package com.nammaexpo.payload.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    @ApiModelProperty(notes = "jwt token")
    private String token;
}
