package com.ps.RESTful.dto.response;

import java.util.List;

import com.ps.dto.UserGroupDTO;
import com.ps.entities.master.CompanyGroupMasterBean;

public class UserGroupResponseDTO extends UserGroupDTO{

	private List<CompanyGroupMasterBean> companyGroupIds;
	private CompanyGroupMasterBean companyGroupMasterBean;

	public List<CompanyGroupMasterBean> getCompanyGroupIds() {
		return companyGroupIds;
	}

	public void setCompanyGroupIds(List<CompanyGroupMasterBean> companyGroupIds) {
		this.companyGroupIds = companyGroupIds;
	}

	public CompanyGroupMasterBean getCompanyGroupMasterBean() {
		return companyGroupMasterBean;
	}

	public void setCompanyGroupMasterBean(CompanyGroupMasterBean companyGroupMasterBean) {
		this.companyGroupMasterBean = companyGroupMasterBean;
	}
	
	
}
