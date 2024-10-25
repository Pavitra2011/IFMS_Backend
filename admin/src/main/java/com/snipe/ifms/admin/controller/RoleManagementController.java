package com.snipe.ifms.admin.controller;

import com.snipe.ifms.admin.dto.RoleManagementDTO;
import com.snipe.ifms.admin.service.RoleManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/roles")
public class RoleManagementController {

    @Autowired
    private RoleManagementService roleManagementService;

    // Endpoint to fetch all roles
    @GetMapping
    public List<RoleManagementDTO> getAllRoles() {
        return roleManagementService.getAllRoles();
    }
}
