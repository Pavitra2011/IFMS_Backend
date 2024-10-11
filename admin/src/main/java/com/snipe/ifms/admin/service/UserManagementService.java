package com.snipe.ifms.admin.service;

import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.repository.UserManagementRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserManagementService {

    @Autowired
    private UserManagementRepository userManagementRepository;

    // Create a new user
    public UserManagementDomain createUser(UserManagementDomain user) {
        return userManagementRepository.save(user);
    }

    // Get user by ID
    public UserManagementDomain getUserById(Long id) {
        Optional<UserManagementDomain> user = userManagementRepository.findById(id);
        return user.orElse(null);
    }

    // Get all users
    public List<UserManagementDomain> getAllUsers() {
        return userManagementRepository.findAll();
    }

    
 // Update a user by ID
    public UserManagementDomain updateUser(Long id, UserManagementDomain updatedUser) {
        Optional<UserManagementDomain> existingUser = userManagementRepository.findById(id);
        if (existingUser.isPresent()) {
            UserManagementDomain user = existingUser.get();
            user.setUserId(updatedUser.getUserId());
            user.setUserName(updatedUser.getUserName());
            user.setMailId(updatedUser.getMailId());
            user.setPhone(updatedUser.getPhone());
            user.setGender(updatedUser.getGender());   // Set gender
            user.setRole(updatedUser.getRole());       // Set role
            user.setStatus(updatedUser.getStatus());   // Set status
            user.setAddress(updatedUser.getAddress()); // Set address        
            user.setDateOfBirth(updatedUser.getDateOfBirth());
            
            return userManagementRepository.save(user);
        }
        return null;
    } 
    
  /*  // Update a user by ID
   *    // user.setDob(updatedUser.getDob());
    public UserManagementDomain updateUser(Long id, UserManagementDomain updatedUser) {
        Optional<UserManagementDomain> existingUser = userManagementRepository.findById(id);
        if (existingUser.isPresent()) {
            UserManagementDomain user = existingUser.get();
            user.setUserId(updatedUser.getUserId());
            user.setUserName(updatedUser.getUserName());
            user.setMailId(updatedUser.getMailId());
            user.setPhone(updatedUser.getPhone());
            user.setGender(updatedUser.getGender());   // Set gender
            user.setRole(updatedUser.getRole());       // Set role
            user.setActive(updatedUser.isActive());   // Set status
            user.setAddress(updatedUser.getAddress()); // Set address
            return userManagementRepository.save(user);
        }
        return null;
    }*/

    // Delete a user by ID
    public boolean deleteUser(Long id) {
        if (userManagementRepository.existsById(id)) {
            userManagementRepository.deleteById(id);
            return true;
        }
        return false;
    }
 // Service method to search users by userName
    public List<UserManagementDomain> searchUsersByName(String userName) {
        return userManagementRepository.findByUserName(userName);
    }
    // Method to search users by mailId
    public List<UserManagementDomain> searchUsersByMailId(String mailId) {
        return userManagementRepository.findByMailId(mailId);
    }
 // Method to search users by phone number
    public List<UserManagementDomain> searchUsersByPhone(String phone) {
        return userManagementRepository.findByPhone(phone);
    }  
}
