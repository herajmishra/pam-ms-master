package com.sunrich.pam.pammsmasters.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.sunrich.pam.pammsmasters.common.Constants;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class VendorDTO {

    private Long id;
    @NotBlank
    private String name;
    private String code;
    private String type;
    private String status;
    private String address;
    private String location;
    @Pattern(regexp = Constants.Patterns.CURRENCY)
    private String currency;
    @Pattern(regexp = Constants.Patterns.TAN_NO)
    private String tanNo;
    @Pattern(regexp = Constants.Patterns.PAN_NO)
    private String panNo;
    @Pattern(regexp = Constants.Patterns.GST_NO)
    private String gst;
}

