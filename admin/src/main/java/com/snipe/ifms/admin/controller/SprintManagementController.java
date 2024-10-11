package com.snipe.ifms.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

//import com.snipe.ifms.admin.domain.SprintManagementDomain;
import com.snipe.ifms.admin.dto.SprintManagementDTO;
//import com.snipe.ifms.admin.service.ProjectManagementService;
import com.snipe.ifms.admin.service.SprintManagementService;

import jakarta.validation.constraints.NotNull;

@RestController
@RequestMapping("/api/sprints")
public class SprintManagementController {

    @Autowired
    private SprintManagementService sprintService;
    //private final ProjectManagementService projectService;  // Inject Project Management service
    // Constructor injection of services
    public SprintManagementController(SprintManagementService sprintManagementService) {
        this.sprintService = sprintManagementService;
        //this.projectService = projectManagementService;
    }

    // Create a new sprint (POST)
    @PostMapping
    public ResponseEntity<SprintManagementDTO> createSprint(@RequestBody SprintManagementDTO sprintDTO) {
        SprintManagementDTO createdSprint = sprintService.createSprint(sprintDTO);
        return ResponseEntity.ok(createdSprint);
    }

    // Delete a sprint by its ID (DELETE)
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteSprint(@PathVariable Long id) {
        boolean isDeleted = sprintService.deleteSprint(id);
        if (isDeleted) {
            return ResponseEntity.ok("Sprint deleted successfully");
        } else {
            return ResponseEntity.status(404).body("Sprint not found");
        }
    }

    // Get all sprints (GET)
    @GetMapping
    public ResponseEntity<List<SprintManagementDTO>> getAllSprints() {
        List<SprintManagementDTO> sprints = sprintService.getAllSprints();
        return ResponseEntity.ok(sprints);
    }

    // Get a sprint by its ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<SprintManagementDTO> getSprintById(@PathVariable Long id) {
        SprintManagementDTO sprint = sprintService.getSprintById(id);
        if (sprint != null) {
            return ResponseEntity.ok(sprint);
        } else {
            return ResponseEntity.status(404).body(null);
        }
    }
    

    /**
     * Get sprints by project ID and return a list of SprintManagementDTO
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getSprintsByProject(@PathVariable @NotNull Long projectId) throws Exception {
        try {
            // Fetch sprints by project ID
            List<SprintManagementDTO> sprints = sprintService.getSprintsByProject(projectId);

            // Check if sprints exist for the given project
            if (sprints.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No sprints found for project with ID " + projectId);
            }

            // Return sprints with a 200 OK status
            return ResponseEntity.ok(sprints);

        } catch (Exception e) {
            // Handle project not found exception
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }
}
