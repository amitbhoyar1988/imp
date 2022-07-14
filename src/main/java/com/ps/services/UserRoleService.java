package com.ps.services;

import java.util.List;

import com.ps.entities.master.UserRole;

public interface UserRoleService {
	List<UserRole> getAll(int companyGroupMasterId);
	public UserRole add(UserRole userRole, int companyGroupMasterId);
	public UserRole update(UserRole userRole, int companyGroupMasterId);
}
