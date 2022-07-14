package com.ps.services.dao.repository.master;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.ps.entities.master.CompanyGroupMasterBean;

@Repository
public interface CompanyGroupMasterBeanRepository extends AbstractRepository<CompanyGroupMasterBean, Integer>{

	@Query(value = "select U.userGroupId,G.companyGroupMasterId, G.companyGroupCode, G.companyGroupName, G.shortName,\r\n"
			+ "U.isActive\r\n"
			+ "from CompanyGroupMaster G, UserGroup U\r\n"
			+ "Where G.companyGroupMasterId In (Select companyGroupMasterId from UserGroup Where groupName = ?1\r\n"
			+ ")\r\n"
			+ "And U.groupName = ?1 \r\n"
			+ "And G.companyGroupMasterId =  U.companyGroupMasterId\r\n"
			+ "And G.isActive = 1 And G.isCompanyGroupActive = 1 And U.companyGroupMasterId In ?2\r\n"
			+ "UNION ALL\r\n"
			+ "select Distinct 0 as userGroupId,G.companyGroupMasterId, G.companyGroupCode, G.companyGroupName, G.shortName,\r\n"
			+ "0 as isActive\r\n"
			+ "from CompanyGroupMaster G\r\n"
			+ "Where G.companyGroupMasterId Not In (Select companyGroupMasterId from UserGroup Where groupName = ?1 \r\n"
			+ ")\r\n"
			+ "And G.isActive = 1 And G.isCompanyGroupActive = 1 And G.companyGroupMasterId In ?2 ", nativeQuery = true)
	List<CompanyGroupMasterBean> findAllCompanyGroupsByUserGroupAndCompanyGroup(String groupName, List<Integer> ids);
	
}
