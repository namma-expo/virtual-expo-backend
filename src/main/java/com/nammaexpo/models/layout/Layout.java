package com.nammaexpo.models.layout;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.nammaexpo.models.layout.herosection.HeroSection;
import com.nammaexpo.models.layout.othersection.Section;
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
@JsonIgnoreProperties(
    ignoreUnknown = true
)
public class Layout {

  private String pageUrl;
  private Theme theme;
  private String logo;
  private HeroSection heroSection;
  private List<Section> otherSections;
}
