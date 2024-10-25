package com.snipe.ifms.admin.domain;


import java.time.LocalDate;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "user_management")
public class UserManagementDomain {

 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id", nullable = false)
    private Long userId; // Changed to Long as primary key with auto-generation
    
    @NotBlank (message = "User name cannot be null")
    @Size(min = 1, max = 255, message = "User name must be between 1 and 255 characters")    
    @Column(name = "user_name", nullable = false, unique = true)
    private String userName;

    
    @NotBlank(message = "Email cannot be blank")
    @Email(message = "Email should be valid")
    @Size(max = 255, message = "Email must be less than 255 characters")
    @Column(name = "mail_id", nullable = false, unique = true)
    private String mailId;

    
 // Phone number validation: must be exactly 10 digits
    @Pattern(regexp = "\\d{10}", message = "Phone number must be exactly 10 digits")
    @Column(name = "phone", nullable = false, unique = true)
    private String phone;

 // Gender validation: cannot be blank
    @NotBlank(message = "Gender cannot be blank")
    @Size(min = 1, max = 50, message = "Gender must be between 1 and 50 characters")
    @Column(name = "gender")
    private String gender;  // Added gender

    


    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "role_name", referencedColumnName = "role_name")
    private RoleManagementDomain role;
    
   
 // Status validation: cannot be blank
    @NotBlank(message = "Status cannot be blank")
    @Column(name = "status", nullable = false)
    private String status;  // Added status

 // Address validation: can be blank, but if present, should be between 1 and 255 characters
    @Size(max = 255, message = "Address must be less than 255 characters")
    @Column(name = "address")
    private String address;
    

    
 // Date of birth field
    @NotNull(message = "Date of birth cannot be blank")
    @Past(message = "Date of birth must be a past date")
    @Column(name = "dob")   
    private LocalDate DateOfBirth;


    
    @CreationTimestamp
    @Column(name = "date_created", updatable = false)
    private LocalDate dateCreated;

    @UpdateTimestamp
    @Column(name = "date_modified")
    private LocalDate dateModified;


    


}
