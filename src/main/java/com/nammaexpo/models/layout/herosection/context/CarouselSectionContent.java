package com.nammaexpo.models.layout.herosection.context;

import com.nammaexpo.models.layout.herosection.CarouselSection;
import com.nammaexpo.models.layout.herosection.HeroSection;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarouselSectionContent extends HeroSection {
    private transient List<CarouselSection> carousels;
}
