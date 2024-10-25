package com.snipe.ifms.admin.service;


import com.snipe.ifms.admin.domain.UserManagementDomain;
import com.snipe.ifms.admin.dto.UserManagementDTO;

import java.util.List;
public interface UserManagementService {
UserManagementDomain createUser(UserManagementDTO userDTO);
    
    UserManagementDTO getUserById(Long id);
    
    List<UserManagementDTO> getAllUsers();
    
    List<UserManagementDTO> getUsersSortedByStatus();
    
    UserManagementDTO updateUser(Long id, UserManagementDTO updatedUserDTO);
    
    boolean deleteUser(Long id);
    
    List<UserManagementDTO> searchUsersByName(String userName);
    
    List<UserManagementDTO> searchUsersByMailId(String mailId);
    
    List<UserManagementDTO> searchUsersByPhone(String phone);
    
    boolean usernameExists(String username);
    
    boolean phoneExists(String phone);
    
    boolean emailExists(String email);
    
    List<UserManagementDTO> getProjectManagers(); 
}
