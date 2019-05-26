package com.sunrich.pam.pammsmasters.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.validation.constraints.NotBlank;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserSaveDTO {

    @NotBlank
    private String userName;
    @NotBlank
    private String password;
    @NonNull
    private Long companyId;
}
