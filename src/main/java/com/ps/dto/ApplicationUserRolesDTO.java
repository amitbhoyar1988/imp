package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApplicationUserRolesDTO {

	private int applicationRoleId;

	private int companyGroupMasterId;

	private int globalCompanyMasterId;

	private String roleName;
	private String roleDiscription;
	private int isActive;
	private String createdBy;

	private String createdDateTime;

	private String lastModifiedBy;

	private String lastModifiedDateTime;

	public ApplicationUserRolesDTO() {
		super();
	}

}
