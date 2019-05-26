package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Commodity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommodityRepository extends JpaRepository<Commodity, Long> {

    List<Commodity> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<Commodity> findByIdAndRecordStatusTrue(Long id);

    Optional<Commodity> findByNameAndRecordStatusTrue(String name);
}
