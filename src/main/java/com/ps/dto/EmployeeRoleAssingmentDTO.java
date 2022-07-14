package com.ps.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class EmployeeRoleAssingmentDTO {

	private int employeeRoleAssignmentId;

	private int globalUserMasterId;

	private int companyGroupMasterId;

	private int globalCompanyMasterId;

	private int userRoleId;

	private String remark;
	private Boolean isActive;
	private String createdBy;

	private String createdDateTime;

	private String lastModifiedBy;

	private String lastModifiedDateTime;

	public EmployeeRoleAssingmentDTO() {
		super();
	}

}
