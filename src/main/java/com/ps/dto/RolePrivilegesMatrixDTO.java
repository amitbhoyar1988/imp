package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class RolePrivilegesMatrixDTO {

	private int rolePrivilegeMatrixId;

	//RelationShip-of-UserRole
	private int userRoleId;

	//RelationShip-of-ApplicationMenu
	private int applicationMenusId;

	//RelationShip-of-GlobalCompanyMaster
	private int globalCompanyMasterId;
	
	private int readAccess;
	private int writeAccess;
	private int modifyAccess;
	private int deleteAccess;
	private int isActive;
	private String createdBy;

	private String createdDateTime;

	private String lastModifiedBy;

	private String lastModifiedDateTime;

	public RolePrivilegesMatrixDTO() {
		super();
	}

}
