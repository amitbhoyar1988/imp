package com.ps.services.dao.repository.master;

import java.util.Optional;

import com.ps.entities.master.GlobalCompanyMaster;

public interface CompanyMasterRepository extends AbstractRepository<GlobalCompanyMaster, Integer> {
	
	Optional<GlobalCompanyMaster> findByCompanyName(String name);
	
	/*
	 * Comment by MayurG: this GroupCompany property is not exist in table
	 * @Query("SELECT cm FROM CompanyMaster cm JOIN FETCH cm.groupCompany "+"WHERE cm.companyMasterId = (:id)")
	 * Optional<CompanyMaster> findByIdAndFetchGroup(@Param("id") int id);
	 */
	
}