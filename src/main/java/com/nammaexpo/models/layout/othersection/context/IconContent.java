package com.nammaexpo.models.layout.othersection.context;

import com.nammaexpo.models.layout.othersection.Section;
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
public class IconContent extends Section {
  private String icon;
  private String heading;
  private String description;
}
