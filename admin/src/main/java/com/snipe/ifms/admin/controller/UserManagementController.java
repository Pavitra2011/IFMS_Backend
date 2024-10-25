package com.snipe.ifms.admin.controller;

import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.UserManagementDTO;
import com.snipe.ifms.admin.exception.ResourceNotFoundException;
import com.snipe.ifms.admin.service.UserManagementServiceImpl;

import jakarta.validation.Valid;

import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/users")
public class UserManagementController {

    @Autowired
    private UserManagementServiceImpl userManagementService;
    
    private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserManagementController.class);


    // Create a new user using DTO
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserManagementDTO userDTO) {
        if (userManagementService.usernameExists(userDTO.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }
        if (userManagementService.phoneExists(userDTO.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists.");
        }
        if (userManagementService.emailExists(userDTO.getMailId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }

        // Convert DTO to domain entity and create the user
        UserManagementDomain createdUser = userManagementService.createUser(userDTO);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }
    
   
    
    
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsername(@RequestParam String username) {
        boolean exists = userManagementService.usernameExists(username);
        return ResponseEntity.ok(exists);
    }
    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam("phone") String phone) {
        if (phone == null || !phone.matches("\\d{10}")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Ensure valid phone number
        }
        boolean exists = userManagementService.phoneExists(phone);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    
    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam("email") String email) {
        if (email == null || !email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Ensure valid email format
        }
        boolean exists = userManagementService.emailExists(email);
        return new ResponseEntity<>(exists, HttpStatus.OK);
    }
    




    // Get user by ID using DTO
    @GetMapping("/{id}")
    public ResponseEntity<UserManagementDTO> getUserById(@PathVariable Long id) {
        UserManagementDTO userDTO = userManagementService.getUserById(id);
        if (userDTO != null) {
            return new ResponseEntity<>(userDTO, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    // Get all users using DTO
    @GetMapping
    public ResponseEntity<List<UserManagementDTO>> getAllUsers() {
        List<UserManagementDTO> users = userManagementService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    

    
    @GetMapping("/with-roles")
    public ResponseEntity<List<UserManagementDTO>> getUsersWithRoles() {
        List<UserManagementDTO> users = userManagementService.getAllUsersWithRoles();
        if (users.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT); // No users found
        }
        return new ResponseEntity<>(users, HttpStatus.OK);
    }


    // Get all users sorted by status
    @GetMapping("/sorted")
    public ResponseEntity<List<UserManagementDTO>> getAllUsersSorted() {
        List<UserManagementDTO> sortedUsers = userManagementService.getUsersSortedByStatus();
        return new ResponseEntity<>(sortedUsers, HttpStatus.OK);
    }

    // Update user using DTO
    @PutMapping("/{id}")
    public ResponseEntity<UserManagementDTO> updateUser(@PathVariable Long id, @Valid @RequestBody UserManagementDTO updatedUserDTO) {
        try {
            UserManagementDTO updatedUser = userManagementService.updateUser(id, updatedUserDTO);
            if (updatedUser != null) {
                return new ResponseEntity<>(updatedUser, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Soft delete a user by ID
  /*  @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userManagementService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }*/

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userManagementService.deleteUser(id);
        
        if (deleted) {
            logger.info("User with ID {} was soft-deleted.", id);
            return ResponseEntity.noContent().build(); // 204 No Content
        } else {
            logger.warn("User with ID {} not found for deletion.", id);
            return ResponseEntity.notFound().build(); // 404 Not Found
        }
    }
    
 /*   //reactivate status after deleting
    @PutMapping("/{userId}/status")
    public ResponseEntity<String> updateUserStatus(@PathVariable Long userId, @RequestBody Map<String, String> updates) {
        String status = updates.get("status");
        
        // Call service to update user status
        userManagementService.updateUserStatus(userId, status);
        
        return ResponseEntity.ok("User status updated successfully");
    }
*/
    
    @PutMapping("/{userId}/status")
    public ResponseEntity<Void> updateUserStatus(@PathVariable Long userId, @RequestBody Map<String, String> statusUpdate) {
        String status = statusUpdate.get("status");
        if (status == null || (!status.equals("Active") && !status.equals("Inactive"))) {
            return ResponseEntity.badRequest().build(); // 400 Bad Request
        }

        try {
            userManagementService.updateUserStatus(userId, status);
            return ResponseEntity.ok().build(); // 200 OK
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build(); // 404 Not Found
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build(); // 500 Internal Server Error
        }
    }




  

    
    // Search users by userName
    @GetMapping("/searchUserByName")
    public ResponseEntity<List<UserManagementDTO>> searchUserByName(@RequestParam("userName") String userName) {
        List<UserManagementDTO> users = userManagementService.searchUsersByName(userName);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    // Search users by mailId
    @GetMapping("/searchByMailId")
    public ResponseEntity<List<UserManagementDTO>> searchUserByMailId(@RequestParam("mailId") String mailId) {
        List<UserManagementDTO> users = userManagementService.searchUsersByMailId(mailId);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }

    // Search users by phone number
    @GetMapping("/searchByPhone")
    public ResponseEntity<List<UserManagementDTO>> searchUserByPhone(@RequestParam("phone") String phone) {
        List<UserManagementDTO> users = userManagementService.searchUsersByPhone(phone);
        if (users.isEmpty()) {
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }
    
 // Endpoint to get project managers as DTOs
    @GetMapping("/project-managers")
    public List<UserManagementDTO> getProjectManagers() {
        return userManagementService.getProjectManagers();
    }
}
