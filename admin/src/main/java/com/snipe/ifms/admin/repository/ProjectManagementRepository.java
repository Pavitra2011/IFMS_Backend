package com.snipe.ifms.admin.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.snipe.ifms.admin.domain.ProjectManagementDomain;

@Repository
public interface ProjectManagementRepository extends JpaRepository<ProjectManagementDomain, Long> {

}
