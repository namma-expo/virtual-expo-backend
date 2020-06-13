package com.nammaexpo.payload.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ContactsDTO {

    @NotBlank
    private String userEmail;
    @NotBlank
    private String userName;
    @Nullable
    private String occupation;
    @Nullable
    private String companyName;
    @Nullable
    private String address1;
    @Nullable
    private String address2;
    @Size(min = 3, max = 3)
    private String country;
    @Size(min = 3, max = 3)
    private String state;
    @Size(min = 3, max = 10)
    private String city;
    @Nullable
    @Size(max = 10)
    private String phone1;
    @Nullable
    @Size(max = 10)
    private String phone2;
    @Nullable
    private String clientReq;
    @Nullable
    private String notes;
}
