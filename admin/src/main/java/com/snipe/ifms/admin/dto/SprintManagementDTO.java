package com.snipe.ifms.admin.dto;

import java.time.LocalDate;

import com.snipe.ifms.admin.domain.SprintStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@NoArgsConstructor
@AllArgsConstructor
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
    private SprintStatus status;

    public SprintManagementDTO(Long sprintId, String sprintName, SprintStatus status, LocalDate startDate, LocalDate endDate, long projectId, String projectName) {
		// TODO Auto-generated constructor stub
    	this.sprintId=sprintId;
    	this.sprintName=sprintName;
    	this.startDate=startDate;
    	this.endDate=endDate;
    	this.projectId=projectId;
    	this.projectName=projectName;
    	this.status = status;
	}

	
}
