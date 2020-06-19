package com.nammaexpo.models.layout.herosection.context;

import com.nammaexpo.models.layout.herosection.HeroSection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VideoSectionContent extends HeroSection {
    private String videoLink;
}
