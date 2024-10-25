package com.snipe.ifms.admin.repository;

import com.snipe.ifms.admin.domain.SprintManagementDomain;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SprintManagementRepository extends JpaRepository<SprintManagementDomain, Long> {

	List<SprintManagementDomain> findByProject_ProjectId(Long projectId);
   
	 
    // Check if a sprint with the same name exists
    boolean existsBySprintName(String sprintName);
    
    // Check if a sprint with the same number exists
    boolean existsBySprintNo(Long sprintNo);
}
