package com.nammaexpo.payload.request;

import io.swagger.annotations.ApiModelProperty;
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
    @ApiModelProperty(notes = "name of the person", required = true)
    private String name;
    @NotBlank
    @ApiModelProperty(notes = "email id of the person", required = true)
    private String email;
    @Nullable
    @ApiModelProperty(notes = "stores queries or remarks")
    private String notes;
    @Nullable
    @ApiModelProperty(notes = "name of the company")
    private String company;
    @Nullable
    @ApiModelProperty(notes = "designation")
    private String occupation;
    @Nullable
    @ApiModelProperty(notes = "primary contact number")
    @Size(max = 10)
    private String phone1;
    @Nullable
    @ApiModelProperty(notes = "secondary contact number")
    @Size(max = 10)
    private String phone2;
}
