package com.ps.dto;

import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@AllArgsConstructor
public class GlobalUserMasterDTO extends AbstractTimeDTO {

	@NotNull(message = "User Name is required!")
	private String userName;
	private int employeeMasterId;
	private String emailId;
	private String mobileNumber;
	private boolean isActive;
	private String activateDate;
	private String deActivateDate;
	private String lastLoginDateTime;
	private String lastPasswordChangeDateTime;
	private boolean loginStatus;
	private boolean isLocked;
	private int failedLoginAttempt;
	private String nextPasswordChangeDateTime;
	private String password;
	private String firstName;
	private String lastName;
	
	
	/*
	 * Below-Parameters-were-missing-before. Added-by-MayurG
	 */
	private int globalCompanyMasterId;
	private int groupDBMasterid;
	private String validTo;
	private String validFrom;
	
	public GlobalUserMasterDTO() {
		super();
	}
}
