package com.sunrich.pam.pammsmasters.repository;

import com.sunrich.pam.pammsmasters.domain.UserOrg;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserOrgRepository extends JpaRepository<UserOrg, Long> {
}
