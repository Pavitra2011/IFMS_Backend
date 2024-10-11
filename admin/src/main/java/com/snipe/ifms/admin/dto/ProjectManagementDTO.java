package com.snipe.ifms.admin.dto;

import java.time.LocalDate;
import java.util.List;

import com.snipe.ifms.admin.domain.ProjectStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ProjectManagementDTO {

    private Long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private String description;
    private boolean isActive;

    // Fields for assignedTo
    private Long assignedToUserId;
    private String assignedToUserName;

    // Sprint and Task IDs
    private List<Long> sprintIds;
    private List<Long> taskIds;
}
