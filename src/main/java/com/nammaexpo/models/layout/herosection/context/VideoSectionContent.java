package com.nammaexpo.models.layout.herosection.context;

import com.nammaexpo.models.layout.herosection.HeroSection;
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
public class VideoSectionContent extends HeroSection {
  private String videoLink;
}
