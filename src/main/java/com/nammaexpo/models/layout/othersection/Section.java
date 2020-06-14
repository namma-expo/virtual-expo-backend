package com.nammaexpo.models.layout.othersection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.nammaexpo.models.layout.othersection.context.IconContent;
import com.nammaexpo.models.layout.othersection.context.ImageContent;
import com.nammaexpo.models.layout.othersection.context.VideoContent;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = As.PROPERTY,
    property = "layoutType",
    visible = true)
@JsonSubTypes({
    @JsonSubTypes.Type(value = IconContent.class, name = "ICON"),
    @JsonSubTypes.Type(value = ImageContent.class, name = "IMAGE"),
    @JsonSubTypes.Type(value = VideoContent.class, name = "VIDEO")
})
@JsonInclude(Include.NON_NULL)
public abstract class Section implements Serializable {
  private SectionLayoutType layoutType;
}
