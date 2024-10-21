package com.snipe.ifms.admin.dto;

import java.time.LocalDate;

import com.snipe.ifms.admin.domain.ProjectManagementDomain;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class SprintManagementDTO {
    private Long sprintId;
    private String sprintName;
    private LocalDate startDate;
    private LocalDate endDate;
    private long projectId;
    private String projectName;
    private Long sprintNo;

    // Getters and setters
    public Long getSprintId() {
        return sprintId;
    }

    public void setSprintId(Long sprintId) {
        this.sprintId = sprintId;
    }

    public String getSprintName() {
        return sprintName;
    }

    public void setSprintName(String sprintName) {
        this.sprintName = sprintName;
    }

    public LocalDate getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Long getProjectId() {
        return projectId;
    }

    public void setProjectId(Long projectId) {
        this.projectId = projectId;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }
}
