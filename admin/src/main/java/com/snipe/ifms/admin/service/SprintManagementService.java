package com.snipe.ifms.admin.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.ifms.admin.domain.SprintManagementDomain;
import com.snipe.ifms.admin.domain.SprintStatus;
import com.snipe.ifms.admin.dto.ProjectManagementDTO;
import com.snipe.ifms.admin.dto.SprintManagementDTO;
import com.snipe.ifms.admin.repository.ProjectManagementRepository;
import com.snipe.ifms.admin.repository.SprintManagementRepository;

import jakarta.transaction.Transactional;

@Service
public class SprintManagementService {

    @Autowired
    private SprintManagementRepository sprintRepository;
    @Autowired
	private ProjectManagementRepository projectManagementRepository;

    // Create a new sprint
    public SprintManagementDTO createSprint(SprintManagementDTO sprintDTO) {
        SprintManagementDomain sprintDomain = convertToEntity(sprintDTO);
        SprintManagementDomain savedSprint = sprintRepository.save(sprintDomain);
        return convertToDTO(savedSprint);
    }

    // Delete a sprint by its ID
    public boolean deleteSprint(Long sprintId) {
        Optional<SprintManagementDomain> sprintOptional = sprintRepository.findById(sprintId);
        if (sprintOptional.isPresent()) {
            sprintRepository.deleteById(sprintId);
            return true;
        }
        return false;  // Sprint not found
    }

    // Get a sprint by its ID
    public SprintManagementDTO getSprintById(Long sprintId) {
        Optional<SprintManagementDomain> sprintOptional = sprintRepository.findById(sprintId);
        return sprintOptional.map(this::convertToDTO).orElse(null);
    }

    // Get all sprints
    public List<SprintManagementDTO> getAllSprints() {
        List<SprintManagementDomain> sprints = sprintRepository.findAll();
        return sprints.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    // Convert SprintManagementDTO to SprintManagementDomain
    private SprintManagementDomain convertToEntity(SprintManagementDTO sprintDTO) {
        SprintManagementDomain sprint = new SprintManagementDomain();
        sprint.setSprintId(sprintDTO.getSprintId());
        sprint.setSprintName(sprintDTO.getSprintName());
        sprint.setStartDate(sprintDTO.getStartDate());
        sprint.setEndDate(sprintDTO.getEndDate());
        sprint.setSprintNo(sprintDTO.getSprintNo());
        sprint.setStatus(sprintDTO.getStatus());
        // Fetch project by ID and set it (optional)
        if (sprintDTO.getProjectId() != 0) {
            sprint.setProject(projectManagementRepository.findById(sprintDTO.getProjectId()).orElse(null));
        }
        return sprint;
    }

    // Convert SprintManagementDomain to SprintManagementDTO
    private SprintManagementDTO convertToDTO(SprintManagementDomain sprint) {
        SprintManagementDTO sprintDTO = new SprintManagementDTO();
        sprintDTO.setSprintId(sprint.getSprintId());
        sprintDTO.setSprintName(sprint.getSprintName());
        sprintDTO.setStartDate(sprint.getStartDate());
        sprintDTO.setEndDate(sprint.getEndDate());
        sprintDTO.setSprintNo(sprint.getSprintNo());
        sprintDTO.setStatus(sprint.getStatus());
     // Set projectId and projectName
        if (sprint.getProject() != null) {
            sprintDTO.setProjectId(sprint.getProject().getProjectId());
            sprintDTO.setProjectName(sprint.getProject().getProjectName());
        }
        return sprintDTO;
    }
    

    /**
     * Get sprints by project ID and return a list of SprintManagementDTO
     */
    public List<SprintManagementDTO> getSprintsByProject(Long projectId) throws Exception {
        // Validate that the project exists using the ProjectRepository
        if (!projectManagementRepository.existsById(projectId)) {
            throw new Exception("Project with ID " + projectId + " does not exist.");
        }

        // Fetch sprints from the repository (assuming sprintRepository returns a list of SprintManagementDomain)
        List<SprintManagementDomain> sprints = sprintRepository.findByProject_ProjectId(projectId);

        // If no sprints were found, you can return an empty list or throw an exception
        if (sprints == null || sprints.isEmpty()) {
            throw new Exception("No sprints found for project with ID " + projectId);
        }

        // Convert SprintManagementDomain entities to SprintManagementDTO
        return sprints.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }
    
    /**
     * Set up a default Sprint for Backlog for every project
     */
    // Create and save a default backlog sprint using SprintManagementDTO
    public SprintManagementDTO createBacklogSprint(ProjectManagementDTO projectDTO) {
        SprintManagementDTO backlogDTO = new SprintManagementDTO();
        backlogDTO.setSprintName("Backlog");
        backlogDTO.setStartDate(LocalDate.now());
        backlogDTO.setEndDate(LocalDate.now().plusYears(5)); // Arbitrary long date
        backlogDTO.setSprintNo(0L);
        backlogDTO.setStatus(SprintStatus.NEW);
        backlogDTO.setProjectId(projectDTO.getProjectId());

        // Convert DTO to domain and save
        SprintManagementDomain backlogSprint = convertToEntity(backlogDTO);
        sprintRepository.save(backlogSprint);

        // Convert saved entity back to DTO and return
        return convertToDTO(backlogSprint);
    }
    
    @Transactional
    public SprintManagementDomain createSprint(SprintManagementDomain sprint) {
        // Check for existing sprint by name or number
        if (sprintRepository.existsBySprintName(sprint.getSprintName())) {
            throw new IllegalArgumentException("Sprint name must be unique.");
        }
        if (sprintRepository.existsBySprintNo(sprint.getSprintNo())) {
            throw new IllegalArgumentException("Sprint number must be unique.");
        }

        return sprintRepository.save(sprint);
    }

    public SprintManagementDTO updateSprint(SprintManagementDTO sprintDTO) throws Exception {
        // Ensure the sprint exists
        SprintManagementDomain existingSprint = sprintRepository.findById(sprintDTO.getSprintId())
                .orElseThrow(() -> new Exception("Sprint not found with ID: " + sprintDTO.getSprintId()));

        // Validate that end date is after start date
        if (sprintDTO.getEndDate().isBefore(sprintDTO.getStartDate())) {
            throw new IllegalArgumentException("End date must be after start date.");
        }

        // Update properties
        existingSprint.setSprintName(sprintDTO.getSprintName());
        existingSprint.setStartDate(sprintDTO.getStartDate());
        existingSprint.setEndDate(sprintDTO.getEndDate());
        existingSprint.setStatus(sprintDTO.getStatus());

        // Save and convert to DTO
        SprintManagementDomain updatedSprint = sprintRepository.save(existingSprint);
        return convertToDTO(updatedSprint);
    }

     
 }
