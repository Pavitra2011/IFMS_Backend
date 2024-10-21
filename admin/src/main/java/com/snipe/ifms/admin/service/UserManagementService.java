package com.snipe.ifms.admin.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.UserManagementDTO;
import com.snipe.ifms.admin.repository.UserManagementRepository;

@Service
public class UserManagementService {

    @Autowired
    private UserManagementRepository userManagementRepository;

    // Create a new user
    public UserManagementDomain createUser(UserManagementDomain user) {
        return userManagementRepository.save(user);          
    }    
 

    
    //added by anu for restricting  duplicate username  and ph no
    public boolean usernameExists(String username) {
        return userManagementRepository.existsByUserName(username);
    }
    
    public boolean phoneExists(String phone) {
        return userManagementRepository.existsByPhone(phone);
    }

 //   public boolean mailIdExists(String mailId) {
   //     return userManagementRepository.existsByPhone(mailId); }
    
    public boolean emailExists(String email) {
        return userManagementRepository.existsBymailId(email);
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

    
    public UserManagementDomain updateUser(Long id, UserManagementDomain updatedUser) {
        UserManagementDomain existingUser = userManagementRepository.findById(id).orElseThrow();
        
        existingUser.setUserName(updatedUser.getUserName());
        existingUser.setMailId(updatedUser.getMailId());
        existingUser.setPhone(updatedUser.getPhone());
        existingUser.setGender(updatedUser.getGender());
        existingUser.setRole(updatedUser.getRole());
        existingUser.setAddress(updatedUser.getAddress());
        existingUser.setStatus(updatedUser.getStatus());
        existingUser.setDateOfBirth(updatedUser.getDateOfBirth());
        
        return userManagementRepository.save(existingUser);
    }
   

    
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
    
    public List<UserManagementDTO> getProjectManagers() {
        // Fetch project managers from the repository
        List<UserManagementDomain> projectManagers = userManagementRepository.findByRole_RoleName("PROJECT_MANAGER");

        // Convert UserManagementDomain to UserManagementDTO
        return projectManagers.stream()
                .map(pm -> new UserManagementDTO(
                        pm.getUserId(),
                        pm.getUserName(),
                        pm.getMailId(),
                        pm.getPhone(),
                        pm.getGender(),
                        pm.getRole() != null ? pm.getRole().getRoleName() : null, // Get role name if available
                        pm.getStatus(),
                        pm.getAddress(),
                        pm.getDateOfBirth(),
                        pm.getDateCreated(),
                        pm.getDateModified()
                ))
                .collect(Collectors.toList());
    }
}
