package com.ps.RESTful.dto.request;

import com.ps.dto.GlobalUserMasterDTO;

public class GlobalUserMasterRequestDTO extends GlobalUserMasterDTO {
	
	EmployeeMasterRequestDTO employeeDetails;

	String oldPassword;
	
	public EmployeeMasterRequestDTO getEmployeeDetails() {
		return employeeDetails;
	}

	public void setEmployeeDetails(EmployeeMasterRequestDTO employeeDetails) {
		this.employeeDetails = employeeDetails;
	}

	public String getOldPassword() {
		return oldPassword;
	}

	public void setOldPassword(String oldPassword) {
		this.oldPassword = oldPassword;
	}
	
}
