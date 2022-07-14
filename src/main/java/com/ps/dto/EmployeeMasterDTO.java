package com.ps.dto;

public class EmployeeMasterDTO {

	private String employeeMasterId;
	private String firstname;
	private String lastname;
	private String employeeCode;
	private String dateOfBirth;
	
	public String getEmployeeCode() {
		return employeeCode;
	}
	public void setEmployeeCode(String employeeCode) {
		this.employeeCode = employeeCode;
	}
	public String getFirstname() {
		return firstname;
	}
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}
	public String getLastname() {
		return lastname;
	}
	public void setLastname(String lastname) {
		this.lastname = lastname;
	}
	public String getEmployeeMasterId() {
		return employeeMasterId;
	}
	public void setEmployeeMasterId(String employeeMasterId) {
		this.employeeMasterId = employeeMasterId;
	}
	public String getDateOfBirth() {
		return dateOfBirth;
	}
	public void setDateOfBirth(String dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
}
