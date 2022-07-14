package com.ps.RESTful.dto.request;

import com.ps.dto.UserRoleDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleRequestDTO extends UserRoleDTO{
//	private int userGroupId;
	private int companyGroupMasterId;
}
