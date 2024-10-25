package com.snipe.ifms.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.snipe.ifms.admin.dto.ProjectManagementDTO;
import com.snipe.ifms.admin.dto.SprintManagementDTO;
import com.snipe.ifms.admin.dto.TaskManagementDTO;
import com.snipe.ifms.admin.service.TaskManagementService;

import jakarta.validation.constraints.NotNull;
//@CrossOrigin(origins = "http://localhost:4200")
@RestController
@RequestMapping("/api/tasks")
public class TaskManagementController {

    @Autowired
    private TaskManagementService taskService;

    // Create a new task (POST)
    @PostMapping
    public ResponseEntity<TaskManagementDTO> createTask(@RequestBody TaskManagementDTO taskDTO) {
        TaskManagementDTO createdTask = taskService.createTask(taskDTO);
        return ResponseEntity.ok(createdTask); // Return 200 OK with the created task
    }

    // Get all tasks (GET)
    @GetMapping
    public ResponseEntity<List<TaskManagementDTO>> getAllTasks() {
        List<TaskManagementDTO> tasks = taskService.getAllTasks();
        return ResponseEntity.ok(tasks); // Return 200 OK with the list of tasks
    }

    // Get task by ID (GET)
    @GetMapping("/{id}")
    public ResponseEntity<TaskManagementDTO> getTaskById(@PathVariable Long id) {
        TaskManagementDTO task = taskService.getTaskById(id);
        if (task != null) {
            return ResponseEntity.ok(task); // Return 200 OK if task is found
        } else {
            return ResponseEntity.notFound().build(); // Return 404 Not Found if task is not found
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        try {
        	taskService.deleteTask(id);
            return ResponseEntity.noContent().build();  // Returns HTTP 204 No Content if successfully deleted
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();  // Returns HTTP 404 if the task wasn't found
        }
    }
    // Update an existing task (PUT)
    @PutMapping("/{id}")
    public ResponseEntity<TaskManagementDTO> updateTask(@PathVariable Long id, @RequestBody TaskManagementDTO taskDTO) {
        try {
            TaskManagementDTO updatedTask = taskService.updateTask(id, taskDTO);
            return ResponseEntity.ok(updatedTask); // Return 200 OK with the updated task
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // Return 404 Not Found if task wasn't found
        }
    }
    
    /**
     * Get tasks by project ID and return a list of TaskManagementDTO
     */
    @GetMapping("/project/{projectId}")
    public ResponseEntity<?> getTasksByProject(@PathVariable @NotNull Long projectId) {
        try {
            // Fetch tasks by project ID
            List<TaskManagementDTO> tasks = taskService.getTasksByProject(projectId);

            // Check if tasks exist for the given project
            if (tasks.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No tasks found for project with ID " + projectId);
            }

            // Return tasks with a 200 OK status
            return ResponseEntity.ok(tasks);

        } catch (Exception e) {
            // Handle any exceptions that may occur
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("An error occurred: " + e.getMessage());
        }
    }
    
    // API to get all projects
    @GetMapping("/projects") // Define the endpoint for retrieving projects
    public ResponseEntity<List<ProjectManagementDTO>> getAllProjects() throws Exception {
        List<ProjectManagementDTO> projects = taskService.getAllProjectsAsDTOs(); // Call the service method
        if (projects.isEmpty()) {
            throw new Exception("No projects found");
        }
        return ResponseEntity.ok(projects); // Return the list of projects as a response
    }
    
 // API to get all sprints
    @GetMapping("/sprints") // Define the endpoint for retrieving sprints
    public ResponseEntity<List<SprintManagementDTO>> getAllSprints() throws Exception {
        List<SprintManagementDTO> sprints = taskService.getAllSprintsAsDTOs(); // Call the service method
        if (sprints.isEmpty()) {
            throw new Exception("No sprints found");
        }
        return ResponseEntity.ok(sprints); // Return the list of sprints as a response
    }
}
