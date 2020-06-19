package com.nammaexpo.models.layout.othersection.context;

import com.nammaexpo.models.layout.othersection.Section;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoContent extends Section {
    private String video;
    private String heading;
    private String description;
}
