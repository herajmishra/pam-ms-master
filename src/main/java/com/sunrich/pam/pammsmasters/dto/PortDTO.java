package com.sunrich.pam.pammsmasters.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PortDTO {

    private Long id;
    @NotBlank
    private String isoPortCode;
    private String description;
    private String state;
    private String postalCode;
    @Email
    private String email;
    private String status;
    @NonNull
    private Long countryId;
    private List<BerthDTO> berths;
}
