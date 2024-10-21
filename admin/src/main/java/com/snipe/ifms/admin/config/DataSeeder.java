package com.snipe.ifms.admin.config;

import java.util.List;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.snipe.ifms.admin.domain.RoleManagementDomain;
import com.snipe.ifms.admin.repository.RoleManagementRepository;

@Configuration
public class DataSeeder {

    @Bean
    CommandLineRunner initRoles(RoleManagementRepository roleRepository) {
        return args -> {
            List<RoleManagementDomain> roles = List.of(
                new RoleManagementDomain(null, "ADMIN", "System Administrator with full access"),
                new RoleManagementDomain(null, "HR_ADMIN", "Manages human resource-related functions"),
                new RoleManagementDomain(null, "PROJECT_MANAGER", "Responsible for project delivery and coordination"),
                new RoleManagementDomain(null, "STUDENT", "Limited access to student resources"),
                new RoleManagementDomain(null, "CHANNEL_PARTNER", "External partner collaborating on business operations"),
                new RoleManagementDomain(null, "VENDOR", "Supplies goods and services"),
                new RoleManagementDomain(null, "INTERNAL_USER", "Internal employee with restricted access")
            );

            // Save roles if they don't already exist
            roles.forEach(role -> {
                if (roleRepository.findByRoleName(role.getRoleName()).isEmpty()) {
                    roleRepository.save(role);
                }
            });
        };
    }
}
