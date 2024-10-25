package com.snipe.ifms.admin.repository;

import com.snipe.ifms.admin.domain.UserManagementDomain;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UserManagementRepository extends JpaRepository<UserManagementDomain, Long> {
    // Custom query methods can be added here, if needed
	//Optional<UserManagementDomain> findByUserName(String userName);
	// Custom query to search users by mailId and phone number
	   @Query("SELECT u FROM UserManagementDomain u WHERE u.userName LIKE %:userName%")
	    List<UserManagementDomain> findByUserName(@Param("userName") String userName);
	 
	// Custom query to search users by mailId
	    @Query("SELECT u FROM UserManagementDomain u WHERE u.mailId = :mailId")
	    List<UserManagementDomain> findByMailId(@Param("mailId") String mailId);
	    
	 // Custom query to search users by phone number
	    @Query("SELECT u FROM UserManagementDomain u WHERE u.phone = :phone")
	    List<UserManagementDomain> findByPhone(@Param("phone") String phone);   
	    //added by anu for restricting duplicate
	    boolean existsByUserName(String userName); // Use the correct field name
	    boolean existsByPhone(String phone);
	    boolean existsBymailId(String mailId);
	    
	    // Custom query to find all active users for soft dlete 
	    @Query("SELECT u FROM UserManagementDomain u WHERE u.status = 'active'")
	    List<UserManagementDomain> findAllActiveUsers();

	 // New method to find users by status
	    List<UserManagementDomain> findByStatus(String status);
	    @Query("SELECT u FROM UserManagementDomain u ORDER BY CASE WHEN u.status = 'active' THEN 0 ELSE 1 END")
	    List<UserManagementDomain> findAllSortedByStatus();
	    
	 // Custom query to fetch users with roles
	    @Query("SELECT u FROM UserManagementDomain u JOIN FETCH u.role")
	    List<UserManagementDomain> findAllWithRoles();
	    
	 // Retrieve all users whose role is "PROJECT_MANAGER"
	    List<UserManagementDomain> findByRole_RoleName(String roleName);
	    
	   

}
