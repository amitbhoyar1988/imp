package com.ps.services.dao.repository.master;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupReport;

public interface UserGroupReportRepository extends AbstractRepository<UserGroupReport, Integer>{
	
	@Query(value = "{call CreateUserGroup(:companyGroupMasterIds, :groupName,"
			+ ":groupDescription, :isDefault, :remark, :isActive, :currentUser, :flag)}", nativeQuery = true)
	String SaveUserGroup(@Param("companyGroupMasterIds") String companyGroupMasterIds,
			@Param("groupName") String groupName,
			@Param("groupDescription") String groupDescription,
			@Param("isDefault") Boolean isDefault,
			@Param("remark") String remark,
			@Param("isActive") Boolean isActive,
			@Param("currentUser") String currentUser,
			@Param("flag") String flag);
	
	@Query(value = "{call CreateUserGroup(:companyGroupMasterIds, :groupName,"
			+ ":groupDescription, :isDefault, :remark, :isActive, :currentUser, :flag)}", nativeQuery = true)
	Optional<UserGroupReport> GetUserGroupDetails(@Param("companyGroupMasterIds") String companyGroupMasterIds,
			@Param("groupName") String groupName,
			@Param("groupDescription") String groupDescription,
			@Param("isDefault") Boolean isDefault,
			@Param("remark") String remark,
			@Param("isActive") Boolean isActive,
			@Param("currentUser") String currentUser,
			@Param("flag") String flag);
	
	@Query(value = "{call CreateUserGroup(:companyGroupMasterIds, :groupName,"
			+ ":groupDescription, :isDefault, :remark, :isActive, :currentUser, :flag)}", nativeQuery = true)
	List<UserGroupReport> findAllDistinctUserGroups (@Param("companyGroupMasterIds") String companyGroupMasterIds,
			@Param("groupName") String groupName,
			@Param("groupDescription") String groupDescription,
			@Param("isDefault") Boolean isDefault,
			@Param("remark") String remark,
			@Param("isActive") Boolean isActive,
			@Param("currentUser") String currentUser,
			@Param("flag") String flag);
	
}
