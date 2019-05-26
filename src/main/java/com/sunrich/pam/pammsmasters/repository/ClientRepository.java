package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.Client;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ClientRepository extends JpaRepository<Client, String> {

    List<Client> findAllByRecordStatusTrue();

    Optional<Client> findByNameAndRecordStatusTrue(String name);

    Optional<Client> findByIdAndRecordStatusTrue(String id);
}
