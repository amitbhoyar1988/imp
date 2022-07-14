package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString 
@AllArgsConstructor
public class UserRoleDTO extends AbstractDTO{
	private int userRoleId;		
	private int userGroupId;
	private String roleName;	
	private String roleDescription;
	private boolean isDefault;	
	private String remark;
	private boolean isActive;
	
	
	public UserRoleDTO() {
		super();
	}
	
	
}
