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
public class VideoContent extends Section {
  private String video;
  private String heading;
  private String description;
}
