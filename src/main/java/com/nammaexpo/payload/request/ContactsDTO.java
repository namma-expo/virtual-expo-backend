package com.nammaexpo.payload.request;

import lombok.*;
import org.springframework.lang.Nullable;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ContactsDTO {

    @NotBlank
    private String name;
    @NotBlank
    private String email;
    @Nullable
    private String notes;
    @Nullable
    private String company;
    @Nullable
    private String occupation;
    @Nullable
    @Size(max = 10)
    private String phone1;
    @Nullable
    @Size(max = 10)
    private String phone2;
}
