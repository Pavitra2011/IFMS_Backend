package com.snipe.ifms.admin.dto;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter // Generates getters for all fields
@Setter
public class UserManagementDTO {
    private Long userId;
    private String userName;
    private String mailId;
    private String phone;
    private String gender;
    private String roleName; // Role name from RoleManagementDomain
    private String status;
    private String address;
    private LocalDate dateOfBirth; // Ensure this matches the type in the domain model
    private LocalDate dateCreated; // Optional: if you want to include this in the DTO
    private LocalDate dateModified; // Optional: if you want to include this in the DTO
}
