package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString 
@AllArgsConstructor
public class UserGroupDTO extends AbstractDTO{
	private int userGroupId;		
	private int companyGroupMasterId;
	private String groupName;	
	private String groupDescription;
	private boolean isDefault;	
	private String remark;
	private boolean isActive;	
	
	public UserGroupDTO() {
		super();
	}
}
