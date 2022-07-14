package com.ps.services;

import java.util.List;

import com.ps.entities.master.RolePrivilegesMatrix;

public interface RolePrivilegesMatrixService {

	// Get-All-Menu-details-by-
	public List<RolePrivilegesMatrix> getMenuesForRoleID(int userRoleID);

	// Add-New-Privileges
	public List<RolePrivilegesMatrix> addPrivileges(List<RolePrivilegesMatrix> requestList);

	// Update-Exist-Privileges
	public List<RolePrivilegesMatrix> updatePrivileges(List<RolePrivilegesMatrix> requestList);
}
