package com.snipe.ifms.admin.controller;

import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.service.UserManagementService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
//import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@CrossOrigin(origins = "http://localhost:4200") // Specify allowed origins
@RestController
@RequestMapping("/api/users")
public class UserManagementController {
	
    @Autowired
    private UserManagementService userManagementService;

    // Create a new user
    @PostMapping
    public ResponseEntity<UserManagementDomain> createUser(@Valid @RequestBody UserManagementDomain user) {
        UserManagementDomain createdUser = userManagementService.createUser(user);
        return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
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

    // Update a user by ID
    @PutMapping("/{id}")
    public ResponseEntity<UserManagementDomain> updateUser(@PathVariable Long id, @RequestBody UserManagementDomain updatedUser) {
        UserManagementDomain user = userManagementService.updateUser(id, updatedUser);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
}


