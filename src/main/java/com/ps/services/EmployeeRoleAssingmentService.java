package com.ps.services;

import java.util.List;

import com.ps.entities.master.EmployeeRoleAssingment;

public interface EmployeeRoleAssingmentService {

	// Get-All-Employee-details-by-UserId
	public List<EmployeeRoleAssingment> getAssignmentDetailsByUserId(int globalUserMasterId);
	
	// Get-All-Employee-details-by-RoleID
	public List<EmployeeRoleAssingment> getMenuesForRoleID(int userRoleId);

	// Get-All-Employee-details-by-RoleID
	public List<EmployeeRoleAssingment> getMenuesForCompanyId(int companyId);

	// Get-All-Employee-details-by-RoleID
	public List<EmployeeRoleAssingment> getEmployeeforCompanyGroupId(int companyGroupId);
	
	//Add-New-EmployeeRoleAssginment-Datails
	public List<EmployeeRoleAssingment> addNewEmployeeRoleAssignment(List<EmployeeRoleAssingment> employeeAssginmentDetails);
	
	//Update-Existing-EmployeeRoleAssginment-Datails
	public List<EmployeeRoleAssingment> updateEmployeeRoleAssignment(List<EmployeeRoleAssingment> employeeAssginmentDetails);

}
