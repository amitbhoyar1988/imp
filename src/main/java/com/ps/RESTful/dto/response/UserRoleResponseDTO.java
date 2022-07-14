package com.ps.RESTful.dto.response;

import com.ps.dto.UserRoleDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserRoleResponseDTO extends UserRoleDTO{
	private String groupName;	
}