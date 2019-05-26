package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    List<User> findAllByRecordStatusTrueOrderByIdDesc();

    Optional<User> findByUserNameAndRecordStatusTrue(String userName);

    Optional<User> findByIdAndRecordStatusTrue(Long id);

    Optional<User> findByUserNameAndPassword(String userName, String password);
}
