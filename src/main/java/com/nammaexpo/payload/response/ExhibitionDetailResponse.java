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
    private String name;
    private String identity;
    private String logo;
    private String url;
    private Layout page;
}
