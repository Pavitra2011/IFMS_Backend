package com.snipe.ifms.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.ifms.admin.domain.SprintManagementDomain;
import com.snipe.ifms.admin.dto.SprintManagementDTO;
import com.snipe.ifms.admin.repository.ProjectManagementRepository;
import com.snipe.ifms.admin.repository.SprintManagementRepository;

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
        // Fetch project by ID and set it (optional)
        if (sprintDTO.getProjectId() != null) {
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
 }
