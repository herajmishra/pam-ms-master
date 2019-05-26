package com.sunrich.pam.pammsmasters.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ClientDTO {

    private String id;
    private String secret;
    private String scope;
    private String name;
    private Short status;
    private Integer refreshTokenDuration;
}
