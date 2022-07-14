package com.ps.services.dao.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;

import com.ps.entities.master.UserRole;

public interface UserRoleRepository extends AbstractRepository<UserRole, Integer>{
	@Query("select e from UserRole e where e.isActive = true ")
	List<UserRole> findAllActive ();
	
//	@Query("select UR.userRoleId, UG.userGroupId, UG.groupName, UR.roleName, UR.roleDescription, UR.remark, UR.isDefault, UR.isActive "
//			+ "from UserRole UR , UserGroup UG "
//			+ "Where UR.isActive = 1 AND UG.userGroupId = UR.userGroup.userGroupId And UR.userGroup.userGroupId In ( "
//			+ "select userGroupId from UserGroup Where companyGroupMasterId = ?1 And isActive = 1) ")
//	List<UserRole> findAllActiveByCompanyGroupId (int companyGroupMasterId);
	
	@Query("select e from UserRole e Where e.isActive = 1 AND e.userGroup.userGroupId In ( "
			+ "select userGroupId from UserGroup Where companyGroupMasterId = ?1 And isActive = 1) ")
	List<UserRole> findAllActiveByCompanyGroupId (int companyGroupMasterId);
	
	@Query("select e from UserRole e where e.userRoleId = ?1 ")
	Optional<UserRole> findByUserRoleId (int userRoleId);
	
	@Query("select e from UserRole e where e.roleName = ?1 and e.userGroup.userGroupId = ?2 ")
	Optional<UserRole> findByUserRoleNameAndUserGroupId (String roleName, int userGroupId);
	
//	@Query("SELECT e FROM UserRole e where e.globalCompanyMaster.globalCompanyMasterId = ?1 and e.isActive = true")
//	Optional<UserRole> findByCompanyId(int companyId);
	
}