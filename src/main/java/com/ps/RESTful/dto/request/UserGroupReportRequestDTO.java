package com.ps.RESTful.dto.request;

import java.util.List;

public class UserGroupReportRequestDTO {	
	String  userGroupName;
	List<Integer> listCompanyGroupIds;
		

	public List<Integer> getListCompanyGroupIds() {
		return listCompanyGroupIds;
	}

	public void setListCompanyGroupIds(List<Integer> listCompanyGroupIds) {
		this.listCompanyGroupIds = listCompanyGroupIds;
	}

	public String getUserGroupName() {
		return userGroupName;
	}

	public void setUserGroupName(String userGroupName) {
		this.userGroupName = userGroupName;
	}	
}
