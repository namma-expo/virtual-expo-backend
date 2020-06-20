package com.nammaexpo.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.nammaexpo.models.layout.Layout;
import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(Include.NON_NULL)
public class ExhibitionDetailResponse {
    private int exhibitionId;
    private String name;
    private String identifier;
    private String logo;
    private Layout layout;
}
