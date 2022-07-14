package com.ps.RESTful.dto.response;

import com.ps.dto.EmployeeRoleAssingmentDTO;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmployeeRoleAssingmentResponseDTO extends EmployeeRoleAssingmentDTO{

	/*
	 * These-Parameters-will-be-used-for-Authorization-purpose-and-dashboard-too
	 */
	private String roleName;
	private int userGroupId;
	private String groupName;
	private String companyGroupName;
	private String companyName;
	
	/*
	 * There-Parameters-will-used-on-dashboard
	 */
	private String userName;
	private int employeeMasterId;
	private String emailId;
	private String mobileNumber;
	private boolean userIsActive;
	private boolean isLocked;

}
