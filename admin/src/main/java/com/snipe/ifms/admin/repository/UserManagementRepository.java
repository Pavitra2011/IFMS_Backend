package com.snipe.ifms.admin.repository;

import com.snipe.ifms.admin.domain.UserManagementDomain;

import java.util.List;
import java.util.Optional;

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
}
