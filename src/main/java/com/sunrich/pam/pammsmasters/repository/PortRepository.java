package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Port;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PortRepository extends JpaRepository<Port, Long> {

    List<Port> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Port> findByIdAndRecordStatusTrue(Long id);

}
