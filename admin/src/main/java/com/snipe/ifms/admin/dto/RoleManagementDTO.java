package com.snipe.ifms.admin.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class RoleManagementDTO {
    private Long roleId;
    private String roleName;
    private String description;
}
