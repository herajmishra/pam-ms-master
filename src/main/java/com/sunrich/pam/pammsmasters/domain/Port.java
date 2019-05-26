package com.sunrich.pam.pammsmasters.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@DynamicUpdate
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Port {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String isoPortCode;
    @Column(nullable = false)
    private String description;
    private String state;
    private String postalCode;
    private String email;
    private String status;
    private Long countryId;
    private Boolean recordStatus;

    @Transient
    private List<Berth> berths;

    @Override
    public String toString() {
        return "Port: {" +
                "id: " + id +
                ", isoPortCode: '" + isoPortCode + '\'' +
                ", description: '" + description + '\'' +
                ", recordStatus: " + recordStatus +
                '}';
    }
}
