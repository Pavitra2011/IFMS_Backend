package com.snipe.ifms.admin.dto;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class TaskManagementDTO {

    private Long taskId;
    private String taskName;
    private String description;
    private String status;
    private LocalDate startDate;
    private LocalDate endDate;
    private Long assignedToUserId;   // ID of the user assigned to the task
    private String assignedToUserName;  // Name of the user assigned to the task
    private Long sprintId;          // ID of the sprint
    private Long projectId;         // ID of the project
    private String priority;        // Priority of the task
}
