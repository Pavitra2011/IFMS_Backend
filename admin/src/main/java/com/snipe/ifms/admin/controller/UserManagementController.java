package com.snipe.ifms.admin.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.UserManagementDTO;
import com.snipe.ifms.admin.service.UserManagementService;

import jakarta.validation.Valid;
//@CrossOrigin(origins = "http://localhost:4200") // Specify allowed origins
@RestController
@RequestMapping("/api/users")
public class UserManagementController {
	
    @Autowired
    private UserManagementService userManagementService;


    
    @PostMapping
    public ResponseEntity<?> createUser(@Valid @RequestBody UserManagementDomain user) {
        // Check if the username already exists
        if (userManagementService.usernameExists(user.getUserName())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Username already exists.");
        }

        // Check if the phone number already exists
        if (userManagementService.phoneExists(user.getPhone())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Phone number already exists.");
        }
        
        // Check if the email already exists
        if (userManagementService.emailExists(user.getMailId())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists.");
        }


        // If checks pass, create the user
        UserManagementDomain createdUser = userManagementService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
    }

    
    @GetMapping("/check-username")
    public ResponseEntity<Boolean> checkUsernameExists(@RequestParam("username") String username) {
        if (username == null || username.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); // Ensure username is valid
        }
        boolean exists = userManagementService.usernameExists(username);
        return new ResponseEntity<>(exists, HttpStatus.OK);
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

    
    
    
    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserManagementDomain> getUserById(@PathVariable Long id) {
        UserManagementDomain user = userManagementService.getUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    

    // Get all users
    @GetMapping
    public ResponseEntity<List<UserManagementDomain>> getAllUsers() {
        List<UserManagementDomain> users = userManagementService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    } 


    

    @PutMapping("/{id}")
    public ResponseEntity<UserManagementDomain> updateUser(@PathVariable Long id, @RequestBody UserManagementDomain updatedUser) {
        try {
            UserManagementDomain user = userManagementService.updateUser(id, updatedUser);
            if (user != null) {
                return new ResponseEntity<>(user, HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Error updating user: " + e.getMessage());
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Delete a user by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userManagementService.deleteUser(id);
        if (deleted) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
 // REST endpoint to search users by userName
    @GetMapping("/searchUserByName")
     public ResponseEntity<List<UserManagementDomain>> searchUserByName(@RequestParam("userName") String userName) {
    //public ResponseEntity<List<UserManagementDomain>> searchUserByName(@PathVariable String userName) {
        List<UserManagementDomain> users = userManagementService.searchUsersByName(userName);

        if (users.isEmpty()) {
        	System.out.println("User does not exist");
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }  
    
 // REST endpoint to search users by mailId
    @GetMapping("/searchByMailId")
    public ResponseEntity<List<UserManagementDomain>> searchUserByMailId(@RequestParam("mailId") String mailId) {
        List<UserManagementDomain> users = userManagementService.searchUsersByMailId(mailId);

        if (users.isEmpty()) {
        	System.out.println("Mailid does not exist for particular user");
            return ResponseEntity.notFound().build();
        } else {
            return ResponseEntity.ok(users);
        }
    }
 // REST endpoint to search users by phone number
    @GetMapping("/searchByPhone")
    public ResponseEntity<List<UserManagementDomain>> searchUserByPhone(@RequestParam("phone") String phone) {
        List<UserManagementDomain> users = userManagementService.searchUsersByPhone(phone);

        if (users.isEmpty()) {
        	System.out.println("PhoneNo does not exist for particular user");
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


