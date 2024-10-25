package com.snipe.ifms.admin.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
//import org.hibernate.validator.internal.util.logging.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.ifms.admin.domain.ProjectManagementDomain;
import com.snipe.ifms.admin.domain.SprintManagementDomain;
import com.snipe.ifms.admin.domain.TaskManagementDomain;
import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.ProjectManagementDTO;
import com.snipe.ifms.admin.dto.SprintManagementDTO;
import com.snipe.ifms.admin.repository.ProjectManagementRepository;
import com.snipe.ifms.admin.repository.SprintManagementRepository;
import com.snipe.ifms.admin.repository.TaskManagementRepository;
import com.snipe.ifms.admin.repository.UserManagementRepository;



@Service
public class ProjectManagementService {
	private static final Logger logger = LoggerFactory.getLogger(ProjectManagementService.class);
	@Autowired
	private  SprintManagementService sprintManagementService;
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
        ProjectManagementDomain project = new ProjectManagementDomain();
        
        // Map simple fields
        project.setProjectId(projectDTO.getProjectId());
        project.setProjectName(projectDTO.getProjectName());
        project.setStartDate(projectDTO.getStartDate());
        project.setEndDate(projectDTO.getEndDate());
        project.setStatus(projectDTO.getStatus());
        project.setDescription(projectDTO.getDescription());
        project.setActive(projectDTO.isActive());
      
        // Fetch assigned users with error handling
     // Fetch assigned users with error handling
        if (projectDTO.getAssignedUserIds() != null) {
            List<UserManagementDomain> assignedUsers = userManagementRepository
                    .findAllById(projectDTO.getAssignedUserIds());
            
            // Handle case where no users are found
            if (assignedUsers.isEmpty()) {
                throw new RuntimeException("No users found for the provided IDs: " + projectDTO.getAssignedUserIds());
            }

            project.setAssignedUsers(assignedUsers);
        }
        
        // Optional: Project Manager Logic
        /*
        if (project.getAssignedUsers() != null && !project.getAssignedUsers().isEmpty()) {
            List<Long> projectManagerIds = userManagementRepository.findByRole_RoleName("PROJECT_MANAGERS")
                    .stream()
                    .map(UserManagementDomain::getUserId)
                    .collect(Collectors.toList());

            List<String> projectManagerNames = userManagementRepository.findByRole_RoleName("PROJECT_MANAGERS")
                    .stream()
                    .map(UserManagementDomain::getUserName)
                    .collect(Collectors.toList());

            System.out.println("ProjectManagerNames: " + projectManagerNames);
            projectDTO.setAssignedUserIds(projectManagerIds);
            projectDTO.setAssignedUserNames(projectManagerNames);
        }*/

        // Set sprints and tasks
        if (projectDTO.getSprintIds() != null && !projectDTO.getSprintIds().isEmpty()) {
            List<SprintManagementDomain> sprints = sprintManagementRepository.findAllById(projectDTO.getSprintIds());
            project.setSprints(sprints);
        }

        if (projectDTO.getTaskIds() != null && !projectDTO.getTaskIds().isEmpty()) {
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
        /*
        if (project.getAssignedTo() != null) {
            UserManagementDomain assignedTo = project.getAssignedTo();
            projectDTO.setAssignedToUserId(assignedTo.getUserId());
            projectDTO.setAssignedToUserName(assignedTo.getUserName());
        }*/
        // Map assigned users
        if (project.getAssignedUsers() != null && !project.getAssignedUsers().isEmpty()) {
            List<Long> userIds = new ArrayList<>();
            List<String> userNames = new ArrayList<>();

            for (UserManagementDomain user : project.getAssignedUsers()) {
                if (user != null) {
                    userIds.add(user.getUserId());
                    userNames.add(user.getUserName());
                }
            }

            projectDTO.setAssignedUserIds(userIds);
            projectDTO.setAssignedUserNames(userNames);
        } else {
            projectDTO.setAssignedUserIds(Collections.emptyList());
            projectDTO.setAssignedUserNames(Collections.emptyList());
        }


        // Map sprints
        if (project.getSprints() != null && !project.getSprints().isEmpty()) {
            List<Long> sprintIds = project.getSprints().stream()
                                         .map(SprintManagementDomain::getSprintId)
                                         .collect(Collectors.toList());
            projectDTO.setSprintIds(sprintIds);
            logger.info("SprintIds from project:"+projectDTO.getSprintIds());
            projectDTO.setSprintNames(
                    project.getSprints().stream()
                    .map(SprintManagementDomain::getSprintName)  // Assuming there's a getSprintName() method
                    .collect(Collectors.toList())
                );
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
    	logger.info("ProjectDTO:"+projectDTO);
        ProjectManagementDomain projectDomain = convertToEntity(projectDTO);
        ProjectManagementDomain savedProject = projectManagementRepository.save(projectDomain);
     // Create a backlog sprint for the project
        SprintManagementDTO backlogSprint = sprintManagementService.createBacklogSprint(
            new ProjectManagementDTO(savedProject.getProjectId(), savedProject.getProjectName())
        );
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
    
 // Method to update a Project
    public ProjectManagementDTO updateProject(Long id, ProjectManagementDTO projectDTO) {
        // Fetch the existing project from the database
        Optional<ProjectManagementDomain> existingProjectOptional = projectManagementRepository.findById(id);

        if (existingProjectOptional.isPresent()) {
            ProjectManagementDomain existingProject = existingProjectOptional.get();

            // Update fields of the existing project with values from projectDTO
            existingProject.setProjectName(projectDTO.getProjectName());
            existingProject.setDescription(projectDTO.getDescription());
            existingProject.setStatus(projectDTO.getStatus());
            existingProject.setStartDate(projectDTO.getStartDate());
            existingProject.setEndDate(projectDTO.getEndDate());
            
            
            // If you need to update the project or sprint, ensure to fetch them if they are also DTOs
            // Example: existingTask.setProject(...);
            // Example: existingTask.setSprint(...);
            // If assignedTo is a DTO, you might need to fetch the user entity based on its ID:
            // UserManagementDomain assignedTo = userRepository.findById(taskDTO.getAssignedToId()).orElse(null);
            // existingTask.setAssignedTo(assignedTo);

            // Save the updated project back to the database
            ProjectManagementDomain updatedProject = projectManagementRepository.save(existingProject);

            // Convert the updated Task entity back to DTO
            return convertToDTO(updatedProject);
        } else {
            throw new RuntimeException("Project not found with ID: " + id); // Handle not found case
        }
    } 
    
    /**
     * Check if a project exists by its ID
     */
    public boolean projectExists(Long projectId) {
        return projectManagementRepository.existsById(projectId);
    }
}
