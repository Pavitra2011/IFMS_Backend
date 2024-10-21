package com.snipe.ifms.admin.repository;

import com.snipe.ifms.admin.domain.SprintManagementDomain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintManagementRepository extends JpaRepository<SprintManagementDomain, Long> {

	List<SprintManagementDomain> findByProject_ProjectId(Long projectId);
    // You can define custom query methods here if needed
    // For example:
    // List<SprintManagementDomain> findByProjectId(Long projectId);
}
