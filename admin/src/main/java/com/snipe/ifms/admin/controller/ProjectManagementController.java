package com.snipe.ifms.admin.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snipe.ifms.admin.domain.ProjectManagementDomain;
import com.snipe.ifms.admin.dto.ProjectManagementDTO;
import com.snipe.ifms.admin.repository.ProjectManagementRepository;
import com.snipe.ifms.admin.service.ProjectManagementService;
@CrossOrigin(origins = "http://localhost:4200") // Specify allowed origins
@RestController
@RequestMapping("/api/projects")
public class ProjectManagementController {

    @Autowired
    private ProjectManagementService projectService;
    @Autowired
    private final ProjectManagementRepository projectRepository;

    ProjectManagementController(ProjectManagementRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    // Create a new project (POST)
    @PostMapping
    public ResponseEntity<ProjectManagementDTO> createProject(@RequestBody ProjectManagementDTO projectDTO) {
        ProjectManagementDTO createdProject = projectService.createProject(projectDTO);
        return ResponseEntity.ok(createdProject);
    }

    // Delete a project by its ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProject(@PathVariable Long id) {
        boolean isDeleted = projectService.deleteProject(id);
        if (isDeleted) {
            return ResponseEntity.ok("Project deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Project not found");
        }
    }

    /**
     * Get all projects
     *
     * @return List of ProjectManagementDTO
     */
    @GetMapping
    public ResponseEntity<List<ProjectManagementDTO>> getAllProjects() {
        List<ProjectManagementDomain> projects = projectRepository.findAll();
        List<ProjectManagementDTO> projectDTOs = projects.stream()
                .map(projectService::convertToDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectDTOs);
    }

    /**
     * Get a project by ID
     *
     * @param projectId Project ID
     * @return ProjectManagementDTO
     */
    @GetMapping("/{projectId}")
    public ResponseEntity<ProjectManagementDTO> getProjectById(@PathVariable Long projectId) {
        ProjectManagementDomain project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projectId));

        ProjectManagementDTO projectDTO = projectService.convertToDTO(project);
        return ResponseEntity.ok(projectDTO);
    }
    
    
}
