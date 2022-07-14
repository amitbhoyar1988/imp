package com.ps.services;

import com.ps.RESTful.dto.request.EmployeeMasterRequestDTO;

public interface EmployeeService {

	public boolean verifyEmployeeDetails(EmployeeMasterRequestDTO requestDTO, String tenantId);
	
}
