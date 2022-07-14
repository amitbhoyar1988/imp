package com.ps.services.dao.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ps.entities.master.GlobalUserMaster;

public interface GlobalUserMasterRepository extends AbstractRepository<GlobalUserMaster, Integer> {
	
	Optional<GlobalUserMaster> findByUserName(String name);
	List<GlobalUserMaster>  findAllByEmailIdOrMobileNumberAndIsActive(String emailId, String mobileNumber, boolean isActive);
	Optional<GlobalUserMaster>  findByEmailIdOrMobileNumberAndIsActive(String emailId, String mobileNumber, boolean isActive);
	Optional<GlobalUserMaster>  findByEmailIdAndMobileNumberAndIsActive(String emailId, String mobileNumber, boolean isActive);
	Optional<GlobalUserMaster>  findByEmailIdOrMobileNumber(String emailId, String mobileNumber);
	
	Optional<GlobalUserMaster> findByEmailId(String emailId);
	
	List<GlobalUserMaster> findALLByMobileNumber(String mobileNumber);
	
	List<GlobalUserMaster> findALLByEmailId(String emailId);
	
	@Query("SELECT u FROM GlobalUserMaster u WHERE u.emailId = :email  and u.isActive = 1")
	Optional<GlobalUserMaster> findActiveByEmailId(@Param("email") String mobile);
	
	@Query("SELECT u FROM GlobalUserMaster u WHERE u.mobileNumber = :mobileNumber  and u.isActive = 1")
	Optional<GlobalUserMaster> findActiveByMobileNumber(@Param("mobileNumber") String mobile);
		
	@Query("SELECT u FROM GlobalUserMaster u WHERE (u.mobileNumber = :mobileNumber or u.emailId = :emailId) and u.password = :password")
	Optional<GlobalUserMaster> findByEmailIdOrMobileNumberAndPassword(@Param("mobileNumber") String mobileNumber,
			@Param("emailId") String emailId,@Param("password") String password);
	
	@Query("SELECT u FROM GlobalUserMaster u JOIN FETCH u.groupDBMaster "
			+ "WHERE u.mobileNumber = :mobile")
	List<GlobalUserMaster> findByMobileNumberAndFetchGroupDBMaster(@Param("mobile") String mobile);
		
	@Query("SELECT u FROM GlobalUserMaster u JOIN FETCH u.groupDBMaster "
			+ "WHERE u.userMasterId = :id")
	Optional<GlobalUserMaster> findByIdAndFetchGroupDBMaster(@Param("id") int id);
	
}