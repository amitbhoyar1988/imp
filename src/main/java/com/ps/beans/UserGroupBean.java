package com.ps.beans;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class UserGroupBean {
	private int userGroupId;
	private String groupName;
	private String groupDescription;
	private int companyGroupMasterId;
	private String companyGroupName;
	private boolean isActive;
	
	public UserGroupBean() {
		super();
	}
}