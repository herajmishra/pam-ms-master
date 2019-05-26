package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Branch;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BranchRepository extends JpaRepository<Branch, Long> {

    List<Branch> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Branch> findByIdAndRecordStatusTrue(Long id);

    Optional<Branch> findByNameAndRecordStatusTrue(String name);

}
