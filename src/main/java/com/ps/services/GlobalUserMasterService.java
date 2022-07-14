package com.ps.services;

import java.util.List;

import com.ps.RESTful.dto.request.EmployeeMasterRequestDTO;
import com.ps.entities.master.GlobalUserMaster;

public interface GlobalUserMasterService {

	public void add(GlobalUserMaster user);
	
	public GlobalUserMaster getById(int id);
	
	public List<GlobalUserMaster> getUsersByCompanyId(int id);
	
	public void addAll(List<GlobalUserMaster> usersList);

	public GlobalUserMaster verifyUserByEmail(String emailId);
	
	public GlobalUserMaster verifyUserByMobile(String mobile);
	
	public GlobalUserMaster verifyUserByMobileAndEmployeeDetails(String mobile,EmployeeMasterRequestDTO employeeDTO);
	
	public void setPassword(GlobalUserMaster user);
	
	public void resetPassword(GlobalUserMaster user, String newPassword);

	public GlobalUserMaster authenticate(GlobalUserMaster user);

}
