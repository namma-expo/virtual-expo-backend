package com.nammaexpo.models.layout.herosection.context;

import com.nammaexpo.models.layout.herosection.CarouselSection;
import com.nammaexpo.models.layout.herosection.HeroSection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarouselSectionContent extends HeroSection {

  private List<CarouselSection> carousels;
}
