package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, Long> {

    List<Organization> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Organization> findByNameAndRecordStatusTrue(String name);

    Optional<Organization> findByIdAndRecordStatusTrue(Long id);
}
