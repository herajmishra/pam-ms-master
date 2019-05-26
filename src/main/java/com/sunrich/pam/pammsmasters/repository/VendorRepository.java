package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface VendorRepository extends JpaRepository<Vendor, Long> {

    List<Vendor> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Vendor> findByIdAndRecordStatusTrue(Long id);

    Optional<Vendor> findByNameAndRecordStatusTrue(String name);

}
