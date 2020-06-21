package com.nammaexpo.models;

import lombok.*;

@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserProfile {

    private String company;

    private String phoneNumber;

    private String country;

    private String state;

    private String city;
}
