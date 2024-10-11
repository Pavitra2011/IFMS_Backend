package com.snipe.ifms.admin.repository;

import com.snipe.ifms.admin.domain.TaskManagementDomain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TaskManagementRepository extends JpaRepository<TaskManagementDomain, Long> {

	List<TaskManagementDomain> findByProject_ProjectId(Long projectId);
}
