package com.snipe.ifms.admin.service;

import com.snipe.ifms.admin.domain.TaskManagementDomain;
import com.snipe.ifms.admin.dto.TaskManagementDTO;
import com.snipe.ifms.admin.repository.TaskManagementRepository;
import com.snipe.ifms.admin.domain.SprintManagementDomain;
import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.domain.ProjectManagementDomain;
import com.snipe.ifms.admin.repository.SprintManagementRepository;
import com.snipe.ifms.admin.repository.UserManagementRepository;
import com.snipe.ifms.admin.repository.ProjectManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class TaskManagementService {

    @Autowired
    private TaskManagementRepository taskManagementRepository;

    @Autowired
    private SprintManagementRepository sprintManagementRepository;

    @Autowired
    private UserManagementRepository userManagementRepository;

    @Autowired
    private ProjectManagementRepository projectManagementRepository;

    // Convert TaskManagementDomain to TaskManagementDTO
    private TaskManagementDTO convertToDTO(TaskManagementDomain taskDomain) {
        return new TaskManagementDTO(
                taskDomain.getTaskId(),
                taskDomain.getTaskName(),
                taskDomain.getDescription(),
                taskDomain.getStatus(),
                taskDomain.getStartDate(),
                taskDomain.getEndDate(),
                taskDomain.getAssignedTo() != null ? taskDomain.getAssignedTo().getUserId() : null,
                taskDomain.getAssignedTo() != null ? taskDomain.getAssignedTo().getUserName() : null,
                taskDomain.getSprint() != null ? taskDomain.getSprint().getSprintId() : null,
                taskDomain.getProject() != null ? taskDomain.getProject().getProjectId() : null,
                taskDomain.getPriority());
    }

    // Convert TaskManagementDTO to TaskManagementDomain
    private TaskManagementDomain convertToEntity(TaskManagementDTO taskDTO) {
        TaskManagementDomain taskDomain = new TaskManagementDomain();
        taskDomain.setTaskId(taskDTO.getTaskId());
        taskDomain.setTaskName(taskDTO.getTaskName());
        taskDomain.setDescription(taskDTO.getDescription());
        taskDomain.setStatus(taskDTO.getStatus());
        taskDomain.setStartDate(taskDTO.getStartDate());
        taskDomain.setEndDate(taskDTO.getEndDate());
        taskDomain.setPriority(taskDTO.getPriority());

        // Fetch the User and Sprint by their IDs and set them
        if (taskDTO.getAssignedToUserId() != null) {
            UserManagementDomain assignedUser = userManagementRepository.findById(taskDTO.getAssignedToUserId())
                    .orElseThrow(() -> new RuntimeException("User not found for ID: " + taskDTO.getAssignedToUserId()));
            taskDomain.setAssignedTo(assignedUser);
        }

        if (taskDTO.getSprintId() != null) {
            SprintManagementDomain sprint = sprintManagementRepository.findById(taskDTO.getSprintId())
                    .orElseThrow(() -> new RuntimeException("Sprint not found for ID: " + taskDTO.getSprintId()));
            taskDomain.setSprint(sprint);
        }

        // Fetch the Project using the projectId from the DTO
        if (taskDTO.getProjectId() != null) {
            ProjectManagementDomain project = projectManagementRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new RuntimeException("Project not found for ID: " + taskDTO.getProjectId()));
            taskDomain.setProject(project);
        }

        return taskDomain;
    }

    /**
     * Fetch all tasks and return a list of TaskManagementDTO.
     */
    public List<TaskManagementDTO> getAllTasks() {
        List<TaskManagementDomain> tasks = taskManagementRepository.findAll();

        return tasks.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    /**
     * Fetch a task by its ID and return a TaskManagementDTO.
     */
    public TaskManagementDTO getTaskById(Long taskId) {
        TaskManagementDomain task = taskManagementRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found for ID: " + taskId));
        return convertToDTO(task);
    }

    /**
     * Create or update a task in the database.
     */
    @Transactional
    public TaskManagementDTO createTask(TaskManagementDTO taskDTO) {
        validateTaskDTO(taskDTO);

        // Convert DTO to Domain entity
        TaskManagementDomain taskDomain = convertToEntity(taskDTO);
        TaskManagementDomain savedTask = taskManagementRepository.save(taskDomain);

        // Convert back to DTO for the response
        return convertToDTO(savedTask);
    }

    /**
     * Delete a task by its ID.
     */
    @Transactional
    public void deleteTask(Long taskId) {
        TaskManagementDomain task = taskManagementRepository.findById(taskId)
                .orElseThrow(() -> new RuntimeException("Task not found for ID: " + taskId));

        taskManagementRepository.delete(task);
    }

    /**
     * Validate the task DTO before creating or updating it.
     */
    private void validateTaskDTO(TaskManagementDTO taskDTO) {
        if (taskDTO.getTaskName() == null || taskDTO.getTaskName().trim().isEmpty()) {
            throw new IllegalArgumentException("Task name is required.");
        }
        if (taskDTO.getStartDate() == null || taskDTO.getEndDate() == null) {
            throw new IllegalArgumentException("Start date and end date are required.");
        }
        if (taskDTO.getStartDate().isAfter(taskDTO.getEndDate())) {
            throw new IllegalArgumentException("Start date cannot be after end date.");
        }

        if (taskDTO.getAssignedToUserId() != null) {
            userManagementRepository.findById(taskDTO.getAssignedToUserId())
                    .orElseThrow(() -> new IllegalArgumentException("Assigned user not found."));
        }

        if (taskDTO.getSprintId() != null) {
            sprintManagementRepository.findById(taskDTO.getSprintId())
                    .orElseThrow(() -> new IllegalArgumentException("Sprint not found."));
        }

        if (taskDTO.getProjectId() != null) {
            projectManagementRepository.findById(taskDTO.getProjectId())
                    .orElseThrow(() -> new IllegalArgumentException("Project not found."));
        }
    }
    
    public List<TaskManagementDTO> getTasksByProject(Long projectId) {
        // Implement the logic to fetch tasks from the database using the repository
        List<TaskManagementDomain> tasks = taskManagementRepository.findByProject_ProjectId(projectId);
        // Convert tasks to TaskManagementDTOs and return
        return tasks.stream().map(task -> convertToDTO(task)).collect(Collectors.toList());
    }
}
