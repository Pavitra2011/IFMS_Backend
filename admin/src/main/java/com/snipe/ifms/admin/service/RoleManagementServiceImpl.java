package com.snipe.ifms.admin.service;

import com.snipe.ifms.admin.domain.RoleManagementDomain;
import com.snipe.ifms.admin.dto.RoleManagementDTO;
import com.snipe.ifms.admin.repository.RoleManagementRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleManagementServiceImpl implements RoleManagementService {

    @Autowired
    private RoleManagementRepository roleManagementRepository;

    // Method to fetch all roles
    @Override
    public List<RoleManagementDTO> getAllRoles() {
        List<RoleManagementDomain> roles = roleManagementRepository.findAll();

        // Convert RoleManagementDomain to RoleManagementDTO
        return roles.stream()
            .map(role -> new RoleManagementDTO(
                role.getRoleId(),
                role.getRoleName(),
                role.getDescription()))
            .collect(Collectors.toList());
    }
}
