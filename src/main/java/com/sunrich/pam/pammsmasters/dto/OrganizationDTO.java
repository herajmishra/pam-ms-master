package com.sunrich.pam.pammsmasters.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunrich.pam.pammsmasters.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.io.File;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrganizationDTO {

    private Long id;
    @NotBlank
    private String name;
    private String aliasName;
    private String shortName;
    private File logo;
    @Email
    private String email;
    @Pattern(regexp = Constants.Patterns.GST_NO)
    private String gstNo;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String state;
    private String country;
}
