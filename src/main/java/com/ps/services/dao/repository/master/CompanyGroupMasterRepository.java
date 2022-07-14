package com.ps.services.dao.repository.master;

import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.ps.entities.master.CompanyGroupMaster;

public interface CompanyGroupMasterRepository extends AbstractRepository<CompanyGroupMaster, Integer> {
	
	@Query("select e from CompanyGroupMaster e where e.isActive = true and e.isCompanyGroupActive = true and e.companyGroupMasterId = ?1 ")
	Optional<CompanyGroupMaster> findActiveCompanyGroup(int companyGroupMasterId);
}
