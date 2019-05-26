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

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BranchDTO {

    private Long id;
    @NotBlank
    private String name;
    private String head;
    private String city;
    @Pattern(regexp = Constants.Patterns.PHONE)
    private String contactNo;
    private String country;
    @Email
    private String email;
    private String state;
    private String street;
}
