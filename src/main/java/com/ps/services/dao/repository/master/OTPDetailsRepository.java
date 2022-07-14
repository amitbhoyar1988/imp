package com.ps.services.dao.repository.master;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.ps.entities.master.OTPMaster;

@Repository
public interface OTPDetailsRepository extends AbstractRepository<OTPMaster, Integer> {
	@Transactional
	@Modifying
	@Query("select o from  OTPMaster o where (o.mobileNumber = :mobileNumber OR o.emailId= :emailId) AND o.isActive = 1")
	List<OTPMaster> activeStatusList(@Param("mobileNumber") String mobileNumber, @Param("emailId") String emailId);

	@Query("select o from OTPMaster o where (o.mobileNumber = :mobileNumber OR o.emailId= :emailId)  AND o.isActive = 1")
	OTPMaster findOTPByEmailIdOrMobile(@Param("emailId") String emailId, @Param("mobileNumber") String mobileNumber);

}
