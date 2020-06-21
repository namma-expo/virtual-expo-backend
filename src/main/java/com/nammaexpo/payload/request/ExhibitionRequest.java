package com.nammaexpo.payload.request;

import lombok.*;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ExhibitionRequest {
    private String name;
    private String logo;
    private String url;
}
