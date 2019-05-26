package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {

    List<Company> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Company> findByNameAndRecordStatusTrue(String name);

    Optional<Company> findByIdAndRecordStatusTrue(Long id);
}
