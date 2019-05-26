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
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CompanyDTO {

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
    @Pattern(regexp = Constants.Patterns.PHONE)
    private String phone;
    private String fax;
    private String regName;
    private String regAddress;
    @Pattern(regexp = Constants.Patterns.PHONE)
    private String regPhone;
    private String regFax;
    @Pattern(regexp = Constants.Patterns.PAN_NO)
    private String pan;
    @Pattern(regexp = Constants.Patterns.TAN_NO)
    private String tan;
    @Pattern(regexp = Constants.Patterns.CURRENCY)
    private String defaultCurrency;
    private Long oldValue;
}
