		package com.snipe.ifms.admin.domain;
		
		import java.time.LocalDate;
		
		import org.hibernate.annotations.CreationTimestamp;
		import org.hibernate.annotations.UpdateTimestamp;
		
		import jakarta.persistence.Column;
		import jakarta.persistence.Entity;
		import jakarta.persistence.GeneratedValue;
		import jakarta.persistence.GenerationType;
		import jakarta.persistence.Id;
		import jakarta.persistence.JoinColumn;
		import jakarta.persistence.ManyToOne;
		import jakarta.persistence.Table;
		import lombok.AllArgsConstructor;
		import lombok.Getter;
		import lombok.NoArgsConstructor;
		import lombok.Setter;
		
		@NoArgsConstructor
		@AllArgsConstructor
		@Getter
		@Setter
		@Entity
		@Table(name = "task_management")
		public class TaskManagementDomain {
		
		    @Id
		    @GeneratedValue(strategy = GenerationType.IDENTITY)
		    @Column(name = "task_id")
		    private Long taskId;
		
		    @Column(name = "task_name", nullable = false)
		    private String taskName;
		
		    @Column(name = "description")
		    private String description;
		
		    @Column(name = "status", nullable = false)
		    private String status;
		
		    @Column(name = "start_date")
		    private LocalDate startDate;
		
		    @Column(name = "end_date")
		    private LocalDate endDate;
		
		 // ManyToOne relationship with ProjectManagementDomain
		    @ManyToOne
		    @JoinColumn(name = "project_id", referencedColumnName = "project_id", nullable = false)
		    private ProjectManagementDomain project;  // Ensure this property name matches `mappedBy` in the other entity
		    
		    @CreationTimestamp
		    @Column(name = "date_created", updatable = false)
		    private LocalDate dateCreated;
		
		    @UpdateTimestamp
		    @Column(name = "date_modified")
		    private LocalDate dateModified;
		    
		    @Column(name = "priority", nullable = false)
		    private String priority;  // e.g., Low, Medium, High
		
		   @ManyToOne
		    @JoinColumn(name = "sprint_id")  // Foreign key reference to SprintManagementDomain
		    private SprintManagementDomain sprint;
	
		    @ManyToOne
		    @JoinColumn(name = "assigned_to")  // Assuming this is a foreign key to UserManagementDomain
		    // Foreign key reference to UserManagementDomain
		    //@Column(name = "assigned_to", nullable = false)
		    private UserManagementDomain assignedTo; // Assuming this is a foreign key to UserManagementDomain
		    /*
		    @ManyToOne
		    @JoinColumn(name = "user_name")
		    */  
		    
		    //private UserManagementDomain userName; 
		    
		}
