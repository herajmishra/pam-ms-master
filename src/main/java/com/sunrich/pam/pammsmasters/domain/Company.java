package com.sunrich.pam.pammsmasters.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import javax.validation.constraints.NotBlank;
import java.io.File;
import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Company {

    @Transient
    List<Organization> organizations;

    @Transient
    List<User> users;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank
    private String name;
    private String aliasName;
    private String shortName;
    private File logo;
    private String email;
    private String gstNo;
    private String address1;
    private String address2;
    private String address3;
    private String city;
    private String state;
    private String country;
    private String phone;
    private String fax;
    private String regName;
    private String regAddress;
    private String  regPhone;
    private String regFax;
    private String pan;
    private String tan;
    private String defaultCurrency;
    private Long oldValue;
    private Boolean recordStatus;
}
