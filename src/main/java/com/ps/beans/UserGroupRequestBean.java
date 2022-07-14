package com.ps.beans;

import java.util.List;

import com.ps.entities.master.CompanyGroupMasterBean;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserGroupRequestBean {	
	private String groupName;	
	private String groupDescription;
	private List<CompanyGroupMasterBean> companyGroupIds;	
	private List<UserGroupBean> companyGroupMasterIds;	
	private boolean isDefault;	
	private String remark;
	private boolean isActive;
	private String createdBy;	
	
	public UserGroupRequestBean() {
		super();
	}	
}
