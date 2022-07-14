package com.ps.services.dao.repository.master;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ps.entities.master.PasswordPolicy;

public interface PasswordPolicyRepository extends AbstractRepository<PasswordPolicy, Integer> {
	
	/*
	 * Commented by MayurG:
	 * @Query("SELECT p FROM PasswordPolicy p JOIN FETCH p.company " + "WHERE p.company.companyMasterId = :companyId")
	 */
	
	@Query("SELECT p FROM PasswordPolicy p JOIN FETCH p.company "
			+ "WHERE p.company.globalCompanyMasterId = :companyId")
	Optional<PasswordPolicy> findByCompanyId(@Param("companyId") int companyId);
	
}