package com.sunrich.pam.pammsmasters.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BerthDTO {

    private Long id;
    @NotNull
    private Long portId;
    @NotBlank
    private String code;
    private String berthDescription;
    private String operation;
    private String maxLoa;
    private String minLoa;
    private String loaUom;
    private String beam;
    private String beamUom;
    private String beamDescription;
    private String uom;
    private String dwt;
    private String dwtUom;
    private String draft;
    private String draftUom;
    private String qty;
    private String swl;
    private String sqlUom;
    private String cargoHandles;
    private String remarks;
    private String status;
}
