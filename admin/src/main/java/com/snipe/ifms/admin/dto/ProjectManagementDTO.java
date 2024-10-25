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

	public ProjectManagementDTO(Long projectId, String projectName, String description) {
        this.projectId = projectId;
        this.projectName = projectName;
        this.description = description;
    }
	public ProjectManagementDTO(Long projectId, String projectName) {
		this.projectId = projectId;
		this.projectName = projectName;
	}
	private Long projectId;
    private String projectName;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private String description;
    private boolean isActive;

    // Fields for assignedTo
 // Assigned managers (optional)
   // private List<Long> assignedUsers;
    private List<Long> assignedUserIds;  // Store user IDs of assigned managers
    private List<String> assignedUserNames;  // Store user names of assigned managers

    // Sprint and Task IDs
    private List<Long> sprintIds;
    private List<String> sprintNames;  // Add this to hold sprint names
    private List<Long> taskIds;
}
