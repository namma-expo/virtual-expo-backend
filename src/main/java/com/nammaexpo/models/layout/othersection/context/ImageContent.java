package com.nammaexpo.models.layout.othersection.context;

import com.nammaexpo.models.layout.othersection.Section;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageContent extends Section {
    private String image;
    private String heading;
    private String description;
}
