package com.snipe.ifms.admin.service;

import com.snipe.ifms.admin.controller.UserManagementController;
import com.snipe.ifms.admin.domain.RoleManagementDomain;
import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.UserManagementDTO;
import com.snipe.ifms.admin.exception.ResourceNotFoundException;
import com.snipe.ifms.admin.repository.RoleManagementRepository;
import com.snipe.ifms.admin.repository.UserManagementRepository;

import jakarta.transaction.Transactional;

import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserManagementServiceImpl implements UserManagementService {

	 @Autowired
	    private UserManagementRepository userManagementRepository;

	 @Autowired
	    private RoleManagementRepository roleManagementRepository;
	 
	 
	 private static final org.slf4j.Logger logger = LoggerFactory.getLogger(UserManagementController.class);
	  
	
	 @Override
	 public UserManagementDomain createUser(UserManagementDTO userDTO) {
	     // Fetch the role by name from the RoleManagementRepository
	     RoleManagementDomain role = roleManagementRepository.findByRoleName(userDTO.getRole())
	         .orElseThrow(() -> new RuntimeException("Role not found with name: " + userDTO.getRole()));

	     // Create a new UserManagementDomain instance
	     UserManagementDomain user = new UserManagementDomain();
	     user.setUserName(userDTO.getUserName());
	     user.setMailId(userDTO.getMailId());
	     user.setPhone(userDTO.getPhone());
	     user.setGender(userDTO.getGender());
	     user.setRole(role); // Set the fetched role object
	     user.setStatus(userDTO.getStatus());
	     user.setAddress(userDTO.getAddress());
	     user.setDateOfBirth(userDTO.getDateOfBirth());

	     // Debugging output
	     System.out.println("Creating user: " + user);
	     System.out.println("With role: " + role);

	    

	     
	     // Save the user
	     return userManagementRepository.save(user);
	 }



   
	 
	 
	  /*  
	   // Create a new user by converting DTO to domain entity
	    @Override
	    public UserManagementDomain createUser(UserManagementDTO userDTO) {
	        UserManagementDomain user = convertToEntity(userDTO);
	        return userManagementRepository.save(user);
	    } */

	    // Get user by ID and convert to DTO
	    @Override
	    public UserManagementDTO getUserById(Long id) {
	        Optional<UserManagementDomain> user = userManagementRepository.findById(id);
	        return user.map(this::convertToDTO).orElse(null);
	    }

	    

	    // Get all users and convert to DTO
	    @Override
	    public List<UserManagementDTO> getAllUsers() {
	        List<UserManagementDomain> users = userManagementRepository.findAll();
	        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

	    // Get users sorted by status and convert to DTO
	    @Override
	    public List<UserManagementDTO> getUsersSortedByStatus() {
	        List<UserManagementDomain> sortedUsers = userManagementRepository.findAllSortedByStatus();
	        return sortedUsers.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

	    
	   
/*
	    // Update user by converting DTO to entity
    @Override
	    public UserManagementDTO updateUser(Long id, UserManagementDTO updatedUserDTO) {
	        UserManagementDomain existingUser = userManagementRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
	        
	        // Update fields
	        existingUser.setUserName(updatedUserDTO.getUserName());
	        existingUser.setMailId(updatedUserDTO.getMailId());
	        existingUser.setPhone(updatedUserDTO.getPhone());
	        existingUser.setGender(updatedUserDTO.getGender());
	        existingUser.setRole(updatedUserDTO.getRole());
	        existingUser.setAddress(updatedUserDTO.getAddress());
	        existingUser.setStatus(updatedUserDTO.getStatus());
	        existingUser.setDateOfBirth(updatedUserDTO.getDateOfBirth());

	        UserManagementDomain updatedUser = userManagementRepository.save(existingUser);
	        return convertToDTO(updatedUser);
	    }*/
	    
	 // Update user by converting DTO to entity
	    @Override
	    public UserManagementDTO updateUser(Long id, UserManagementDTO updatedUserDTO) {
	        UserManagementDomain existingUser = userManagementRepository.findById(id)
	                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + id));
	        
	        // Update fields
	        existingUser.setUserName(updatedUserDTO.getUserName());
	        existingUser.setMailId(updatedUserDTO.getMailId());
	        existingUser.setPhone(updatedUserDTO.getPhone());
	        existingUser.setGender(updatedUserDTO.getGender());

	        // Fetch the role by role name for the update
	        RoleManagementDomain role = roleManagementRepository.findByRoleName(updatedUserDTO.getRole())
	                .orElseThrow(() -> new RuntimeException("Role not found with name: " + updatedUserDTO.getRole()));
	        
	        // Log the fetched role details
	        logger.info("Fetched role: " + role.getRoleName() + " with ID: " + role.getRoleId());
	     // Set the fetched role in the user entity
	        existingUser.setRole(role);
	        
	        existingUser.setAddress(updatedUserDTO.getAddress());
	        existingUser.setStatus(updatedUserDTO.getStatus());
	        existingUser.setDateOfBirth(updatedUserDTO.getDateOfBirth());

	        UserManagementDomain updatedUser = userManagementRepository.save(existingUser);
	        return convertToDTO(updatedUser);
	    }
	    


	    public List<UserManagementDTO> getAllUsersWithRoles() {
	        List<UserManagementDomain> users = userManagementRepository.findAllWithRoles();
	        return users.stream()
	                .map(user -> {
	                    UserManagementDTO dto = new UserManagementDTO();
	                    dto.setUserId(user.getUserId());
	                    dto.setUserName(user.getUserName());
	                    dto.setMailId(user.getMailId());
	                    dto.setPhone(user.getPhone());
	                    dto.setGender(user.getGender());
	                    dto.setStatus(user.getStatus());
	                    dto.setAddress(user.getAddress());
	                    dto.setDateOfBirth(user.getDateOfBirth());

	                 // Map the role name from RoleManagementDomain
	                    if (user.getRole() != null) {
	                        dto.setRole(user.getRole().getRoleName()); // Set the role name directly
	                    }

	                    return dto;
	                })
	                .collect(Collectors.toList());
	    }



	    // Soft delete user by setting status to inactive
	    @Override
	    public boolean deleteUser(Long id) {
	        Optional<UserManagementDomain> userOptional = userManagementRepository.findById(id);
	        if (userOptional.isPresent()) {
	            UserManagementDomain user = userOptional.get();
	            user.setStatus("Inactive");
	            userManagementRepository.save(user);
	            return true;
	        }
	        return false;
	    }

	    public void updateUserStatus(Long userId, String status) {
	        if (!status.equals("Active") && !status.equals("Inactive")) {
	            throw new IllegalArgumentException("Invalid status: " + status);
	        }

	        // Fetch the user from the repository
	        UserManagementDomain user = userManagementRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

	        // Set the new status
	        user.setStatus(status);

	        // Save the updated user entity
	        userManagementRepository.save(user);
	    }


	    
	  /*  
	    public void updateUserStatus(Long userId, String status) {
	        // Fetch the user from the repository using the domain class
	        UserManagementDomain user = userManagementRepository.findById(userId)
	            .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

	        // Set the new status
	        user.setStatus(status);

	        // Save the updated user entity, dateModified will be automatically updated
	        userManagementRepository.save(user);
	    }
*/


	    // Search users by userName and convert to DTO
	    @Override
	    public List<UserManagementDTO> searchUsersByName(String userName) {
	        List<UserManagementDomain> users = userManagementRepository.findByUserName(userName);
	        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

	    // Search users by mailId and convert to DTO
	    @Override
	    public List<UserManagementDTO> searchUsersByMailId(String mailId) {
	        List<UserManagementDomain> users = userManagementRepository.findByMailId(mailId);
	        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

	    // Search users by phone and convert to DTO
	    @Override
	    public List<UserManagementDTO> searchUsersByPhone(String phone) {
	        List<UserManagementDomain> users = userManagementRepository.findByPhone(phone);
	        return users.stream().map(this::convertToDTO).collect(Collectors.toList());
	    }

	    // Check if username exists
	    @Override
	    public boolean usernameExists(String username) {
	        return userManagementRepository.existsByUserName(username);
	    }

	    // Check if phone exists
	    @Override
	    public boolean phoneExists(String phone) {
	        return userManagementRepository.existsByPhone(phone);
	    }

	    // Check if email exists
	    @Override
	    public boolean emailExists(String email) {
	        return userManagementRepository.existsBymailId(email);
	    }

	    // Convert UserManagementDomain to UserManagementDTO
	    private UserManagementDTO convertToDTO(UserManagementDomain user) {
	        UserManagementDTO userDTO = new UserManagementDTO();
	        userDTO.setUserId(user.getUserId());
	        userDTO.setUserName(user.getUserName());
	        userDTO.setMailId(user.getMailId());
	        userDTO.setPhone(user.getPhone());
	        userDTO.setGender(user.getGender());   
	        
	      //  userDTO.setRole(user.getRole());
	        
	     // Get role name from the role entity
	        if (user.getRole() != null) {
	            userDTO.setRole(user.getRole().getRoleName()); // Set role name	           
	        }
	        
	        userDTO.setStatus(user.getStatus());
	        userDTO.setAddress(user.getAddress());
	        userDTO.setDateOfBirth(user.getDateOfBirth());	
	        userDTO.setDateCreated(user.getDateCreated()); // Set created date
	        userDTO.setDateModified(user.getDateModified()); // Set modified date

	        return userDTO;
	    }

	    // Convert UserManagementDTO to UserManagementDomain
	    private UserManagementDomain convertToEntity(UserManagementDTO userDTO) {
	        UserManagementDomain user = new UserManagementDomain();
	        user.setUserName(userDTO.getUserName());
	        user.setMailId(userDTO.getMailId());
	        user.setPhone(userDTO.getPhone());
	        user.setGender(userDTO.getGender());     
		//        user.setRole(userDTO.getRole());
	        
	     // Fetch the role based on role name
	        RoleManagementDomain role = roleManagementRepository.findByRoleName(userDTO.getRole())
	            .orElseThrow(() -> new RuntimeException("Role not found with name: " + userDTO.getRole()));
	        user.setRole(role); // Set the fetched role
	        
	        
	        user.setStatus(userDTO.getStatus());
	        user.setAddress(userDTO.getAddress());
	        user.setDateOfBirth(userDTO.getDateOfBirth());
	        return user;
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
