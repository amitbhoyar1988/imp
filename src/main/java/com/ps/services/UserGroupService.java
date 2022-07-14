package com.ps.services;

import java.util.List;

import com.ps.beans.UserGroupBean;
import com.ps.beans.UserGroupRequestBean;
import com.ps.entities.master.CompanyGroupMasterBean;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupReport;

public interface UserGroupService {
	List<UserGroup> getAllUserGroupsByCompanyGroupIds(List<Integer> companyGroupMasterIds);
	List<UserGroupReport> getAllDistinctUserGroups();
	List<UserGroupBean> getAllUserGroupsByCompanyGroupId(Integer CompanyGroupId);
	List<UserGroupBean> getAllCompanyGroupByUserGroupName(String groupName, List<Integer> companyGroupMasterIds);
	List<UserGroupBean> getAllDistinctUserGroups(List<Integer> companyGroupMasterIds);
	List<CompanyGroupMasterBean> getAllCompanyGroupByUserGroups(String groupName, List<Integer> listCompanyGroupIds);
//	List<UserGroup> getAllCompanyGroupsByUserGroup(Integer CompanyGroupId);
	public List<UserGroup> addAllUserGroup(List<UserGroup> userGroupList);	
	public String addAll(UserGroupRequestBean userGroupBean);
//	List<UserRole> getAllUserRole();
	public UserGroupRequestBean getUserGroupDetails(String groupName);
}
