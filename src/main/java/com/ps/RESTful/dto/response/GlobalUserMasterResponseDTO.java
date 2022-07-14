package com.ps.RESTful.dto.response;

import com.ps.dto.GlobalUserMasterDTO;

public class GlobalUserMasterResponseDTO extends GlobalUserMasterDTO {
	
	private int globalUserMasterId;	
	private GlobalCompanyMasterResponseDTO company;
	private String passwordDueDateTime;
	
	public int getGlobalUserMasterId() {
		return globalUserMasterId;
	}
	public void setGlobalUserMasterId(int id) {
		this.globalUserMasterId = id;
	}
	public GlobalCompanyMasterResponseDTO getCompany() {
		return company;
	}
	public void setCompany(GlobalCompanyMasterResponseDTO company) {
		this.company = company;
	}
	public String getPasswordDueDateTime() {
		return passwordDueDateTime;
	}
	public void setPasswordDueDateTime(String passwordDueDateTime) {
		this.passwordDueDateTime = passwordDueDateTime;
	}
	
}
