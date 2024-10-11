package com.snipe.ifms.admin.service;

import java.time.LocalDate;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class TaskManagementDto{

    private Long taskId;          // For identifying the task (required for updates)
    private String taskName;      // The name of the task (required)
    private String description;   // A description of the task (optional)
    private String status;        // The current status of the task (required)
    private LocalDate startDate;  // The start date of the task (optional)
    private LocalDate endDate;    // The end date of the task (optional)
    private Long projectId;       // The ID of the associated project (required)
}