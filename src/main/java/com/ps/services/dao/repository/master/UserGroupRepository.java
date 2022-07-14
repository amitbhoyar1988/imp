package com.ps.services.dao.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.ps.beans.UserGroupBean;
import com.ps.entities.master.CompanyGroupMasterBean;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupReport;

public interface UserGroupRepository extends AbstractRepository<UserGroup, Integer>{
	
	@Query(value = "select * from UserGroup where companyGroupMasterId = ?1  and isActive = 1 ", nativeQuery = true)
	List<UserGroup> findByCompanyGroupId (int companyGroupId);
		
	@Query("select e from UserGroup e where e.isActive = true And e.companyGroupMaster.companyGroupMasterId In ?1 ")
	List<UserGroup> findAllActiveByCompanyGroupId (List<Integer> companyGroupMasterIds);
	
	@Query("select e from UserGroup e where e.isActive = true ")
	List<UserGroup> findAllActiveBy ();
	
	@Query("select e from UserGroup e where e.userGroupId = ?1 And e.companyGroupMaster.companyGroupMasterId = ?2")
	Optional<UserGroup> findByUserGroupAndCompanyGroupId (int userGroupId, int companyGroupMasterId);
	
	@Query("select e from UserGroup e where e.groupName = ?1 And e.companyGroupMaster.companyGroupMasterId = ?2")
	Optional<UserGroup> findByGroupNameAndCompanyGroupId (String groupName, int companyGroupMasterId);
	
	@Query(value = "select distinct 0 as userGroupId, groupName, groupDescription, '' As companyGroupIds,\r\n"
			+ "isDefault, remark, isActive, '' as createdBy from UserGroup where isActive = 1 ", nativeQuery = true)
	List<UserGroupReport> findAllDistinctUserGroups ();
	
		
	// get user group data by active
		Optional<UserGroup> findByuserGroupId(int userGroupId);
		
		@Query("select  new com.ps.beans.UserGroupBean (e.userGroupId, e.groupName, e.groupDescription, e.companyGroupMaster.companyGroupMasterId, e.companyGroupMaster.companyGroupName, e.isActive) "
				+ "from UserGroup e where  e.isActive = true And e.companyGroupMaster.companyGroupMasterId = ?1")
		List<UserGroupBean> findAllUserGroupsByCompanyGroupId(int companyGroupMasterId);
		
		@Query("select  new com.ps.beans.UserGroupBean (e.userGroupId, e.groupName, e.groupDescription, e.companyGroupMaster.companyGroupMasterId, e.companyGroupMaster.companyGroupName, e.isActive) "
				+ "from UserGroup e where  e.isActive = true And e.groupName = ?1 And e.companyGroupMaster.companyGroupMasterId In ?2")
		List<UserGroupBean> findAllCompanyGroupNamesByUserGroupName(String groupName, List<Integer> companyGroupMasterIds);
		
		@Query("select  new com.ps.beans.UserGroupBean (0 as userGroupId, e.groupName, e.groupDescription, 0 as companyGroupMasterId, '' as companyGroupName, e.isActive) "
				+ "from UserGroup e where  e.isActive = true And e.companyGroupMaster.isCompanyGroupActive = 1 And e.companyGroupMaster.isActive = 1 And e.companyGroupMaster.companyGroupMasterId In ?1")
		List<UserGroupBean> findDistinctUserGroups(List<Integer> companyGroupMasterIds);
				
}
