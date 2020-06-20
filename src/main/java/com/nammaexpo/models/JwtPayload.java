package com.nammaexpo.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.nammaexpo.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class JwtPayload {
    private String email;

    private String identity;

    private Role role;
}
