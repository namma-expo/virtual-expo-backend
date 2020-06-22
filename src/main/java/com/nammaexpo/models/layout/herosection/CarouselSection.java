package com.nammaexpo.models.layout.herosection;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CarouselSection implements Serializable {

    private static final long serialVersionUID = 8911181587562931633L;

    private transient String image;
    private transient String heading;
    private transient String description;
}
