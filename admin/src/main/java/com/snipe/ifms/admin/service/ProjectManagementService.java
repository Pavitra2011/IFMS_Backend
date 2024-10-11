package com.snipe.ifms.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.ifms.admin.domain.ProjectManagementDomain;
import com.snipe.ifms.admin.domain.SprintManagementDomain;
import com.snipe.ifms.admin.domain.TaskManagementDomain;
import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.ProjectManagementDTO;
import com.snipe.ifms.admin.repository.ProjectManagementRepository;
import com.snipe.ifms.admin.repository.SprintManagementRepository;
import com.snipe.ifms.admin.repository.TaskManagementRepository;
import com.snipe.ifms.admin.repository.UserManagementRepository;

@Service
public class ProjectManagementService {

    @Autowired
    private ProjectManagementRepository projectManagementRepository;

    @Autowired
    private UserManagementRepository userManagementRepository; // To fetch UserManagementDomain by userId

    @Autowired
    private SprintManagementRepository sprintManagementRepository; // To fetch SprintManagementDomain by sprintId

    @Autowired
    private TaskManagementRepository taskManagementRepository; // To fetch TaskManagementDomain by taskId

    /**
     * Convert DTO to Entity (used for creating or updating a Project)
     *
     * @param projectDTO ProjectManagementDTO
     * @return ProjectManagementDomain entity
     */
    public ProjectManagementDomain convertToEntity(ProjectManagementDTO projectDTO) {
        // Create a new ProjectManagementDomain instance
        ProjectManagementDomain project = new ProjectManagementDomain();
        
        // Map simple fields
        project.setProjectId(projectDTO.getProjectId());
        project.setProjectName(projectDTO.getProjectName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatus(projectDTO.getStatus());
        project.setDescription(projectDTO.getDescription());
        project.setActive(projectDTO.isActive());
        
        // Map the assignedTo field
        if (projectDTO.getAssignedToUserId() != null) {
            // Look up the user by userId and set the assignedTo field
            UserManagementDomain assignedToUser = userManagementRepository.findById(projectDTO.getAssignedToUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + projectDTO.getAssignedToUserId()));
            project.setAssignedTo(assignedToUser);
        }

        // Set the sprints and tasks
        if (projectDTO.getSprintIds() != null && !projectDTO.getSprintIds().isEmpty()) {
            // Look up sprints by their IDs and set them in the project
            List<SprintManagementDomain> sprints = sprintManagementRepository.findAllById(projectDTO.getSprintIds());
            project.setSprints(sprints);
        }

        if (projectDTO.getTaskIds() != null && !projectDTO.getTaskIds().isEmpty()) {
            // Look up tasks by their IDs and set them in the project
            List<TaskManagementDomain> tasks = taskManagementRepository.findAllById(projectDTO.getTaskIds());
            project.setTasks(tasks);
        }

        return project;
    }
    
    /**
     * Convert ProjectManagementDomain entity to ProjectManagementDTO
     *
     * @param project ProjectManagementDomain
     * @return ProjectManagementDTO
     */
    public ProjectManagementDTO convertToDTO(ProjectManagementDomain project) {
        // Create a new ProjectManagementDTO instance
        ProjectManagementDTO projectDTO = new ProjectManagementDTO();

        // Map simple fields
        projectDTO.setProjectId(project.getProjectId());
        projectDTO.setProjectName(project.getProjectName());
        projectDTO.setStartDate(project.getStartDate());
        projectDTO.setEndDate(project.getEndDate());
        projectDTO.setStatus(project.getStatus());
        projectDTO.setDescription(project.getDescription());
        projectDTO.setActive(project.isActive());

        // Map the assignedTo field
        if (project.getAssignedTo() != null) {
            UserManagementDomain assignedTo = project.getAssignedTo();
            projectDTO.setAssignedToUserId(assignedTo.getUserId());
            projectDTO.setAssignedToUserName(assignedTo.getUserName());
        }

        // Map sprints
        if (project.getSprints() != null && !project.getSprints().isEmpty()) {
            List<Long> sprintIds = project.getSprints().stream()
                                         .map(SprintManagementDomain::getSprintId)
                                         .collect(Collectors.toList());
            projectDTO.setSprintIds(sprintIds);
        }

        // Map tasks
        if (project.getTasks() != null && !project.getTasks().isEmpty()) {
            List<Long> taskIds = project.getTasks().stream()
                                       .map(TaskManagementDomain::getTaskId)
                                       .collect(Collectors.toList());
            projectDTO.setTaskIds(taskIds);
        }

        return projectDTO;
    }

    // Other methods for handling Project CRUD operations
    
    // Create a new project
    public ProjectManagementDTO createProject(ProjectManagementDTO projectDTO) {
        ProjectManagementDomain projectDomain = convertToEntity(projectDTO);
        ProjectManagementDomain savedProject = projectManagementRepository.save(projectDomain);
        return convertToDTO(savedProject);
    }

    // Delete a project by its ID
    public boolean deleteProject(Long projectId) {
        Optional<ProjectManagementDomain> projectOptional = projectManagementRepository.findById(projectId);
        if (projectOptional.isPresent()) {
            projectManagementRepository.deleteById(projectId);
            return true;
        } else {
            return false;  // Project not found
        }
    }

    // Get a project by its ID
    public ProjectManagementDTO getProjectById(Long projectId) {
        Optional<ProjectManagementDomain> projectOptional = projectManagementRepository.findById(projectId);
        return projectOptional.map(this::convertToDTO).orElse(null);
    }
    
    // Get all projects
    public List<ProjectManagementDTO> getAllProjects() {
        List<ProjectManagementDomain> projects = projectManagementRepository.findAll();
        return projects.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    /**
     * Check if a project exists by its ID
     */
    public boolean projectExists(Long projectId) {
        return projectManagementRepository.existsById(projectId);
    }
}
