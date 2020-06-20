package com.nammaexpo.models.layout.herosection.context;

import com.nammaexpo.models.layout.herosection.HeroSection;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ImageSectionContent extends HeroSection {
    private String image;
    private String heading;
    private String description;
}
