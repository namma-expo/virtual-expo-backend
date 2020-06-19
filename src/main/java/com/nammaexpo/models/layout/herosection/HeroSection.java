package com.nammaexpo.models.layout.herosection;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.nammaexpo.models.layout.herosection.context.CarouselSectionContent;
import com.nammaexpo.models.layout.herosection.context.ImageSectionContent;
import com.nammaexpo.models.layout.herosection.context.VideoSectionContent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

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
        @JsonSubTypes.Type(value = CarouselSectionContent.class, name = "CAROUSEL"),
        @JsonSubTypes.Type(value = ImageSectionContent.class, name = "IMAGE"),
        @JsonSubTypes.Type(value = VideoSectionContent.class, name = "VIDEO")
})
@JsonInclude(Include.NON_NULL)
public abstract class HeroSection implements Serializable {
    private HeroLayoutType layoutType;
}
