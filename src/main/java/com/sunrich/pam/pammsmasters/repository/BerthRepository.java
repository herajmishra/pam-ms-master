package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Berth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BerthRepository extends JpaRepository<Berth, Long> {

    List<Berth> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Berth> findByIdAndRecordStatusTrue(Long id);

    List<Berth> findAllByPortIdAndRecordStatusTrueOrderByIdDesc(Long portId);

    Optional<Berth> findByCodeContaining(Long aLong);
}
