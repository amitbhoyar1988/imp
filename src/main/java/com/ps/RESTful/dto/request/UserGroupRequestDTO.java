package com.ps.RESTful.dto.request;

import java.util.List;

import com.ps.beans.UserGroupBean;
import com.ps.dto.UserGroupDTO;
import com.ps.entities.master.CompanyGroupMasterBean;

public class UserGroupRequestDTO extends UserGroupDTO{
private List<CompanyGroupMasterBean> companyGroupMasterIdList;
private List<UserGroupBean> companyGroupMasterIds;	
private List<Integer> groupIds;

public List<Integer> getGroupIds() {
	return groupIds;
}

public void setGroupIds(List<Integer> groupIds) {
	this.groupIds = groupIds;
}

public List<UserGroupBean> getCompanyGroupMasterIds() {
	return companyGroupMasterIds;
}

public void setCompanyGroupMasterIds(List<UserGroupBean> companyGroupMasterIds) {
	this.companyGroupMasterIds = companyGroupMasterIds;
}

public List<CompanyGroupMasterBean> getCompanyGroupMasterIdList() {
	return companyGroupMasterIdList;
}

public void setCompanyGroupMasterIdList(List<CompanyGroupMasterBean> companyGroupMasterIdList) {
	this.companyGroupMasterIdList = companyGroupMasterIdList;
}

}
