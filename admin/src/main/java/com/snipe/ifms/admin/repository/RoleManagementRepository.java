package com.snipe.ifms.admin.repository;

import com.snipe.ifms.admin.domain.RoleManagementDomain;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleManagementRepository extends JpaRepository<RoleManagementDomain, Long> {
    Optional<RoleManagementDomain> findByRoleName(String roleName);
}
