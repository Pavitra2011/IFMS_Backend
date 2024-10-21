package com.snipe.ifms.admin.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

	@Entity
	@Table(name = "roles")
	@Getter @Setter
	@NoArgsConstructor @AllArgsConstructor
	public class RoleManagementDomain {
	
	    @Id
	    @GeneratedValue(strategy = GenerationType.IDENTITY)
	    @Column(name = "role_id")
	    private Long roleId;
	
	    @NotBlank
	    @Column(name = "role_name", unique = true, nullable = false)
	    private String roleName;
	    
	    @Column(name = "description", nullable = true)
	    private String description;
	}
	
