package com.ps.entities.master;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.ToString;

@Entity
@Table(name = "OtpTransactionMaster")
@ToString
public class OTPMaster {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otpTransactionMasterId")
	private int otpId;

	@Column(name = "applicationMasterId")
	private int applicationId;

	@Column(name = "countryCodeId")
	private int countryCodeId;

	@Column(name = "mobileNumber")
	private String mobileNumber;

	@Column(name = "emailId")
	private String emailId;

	@Column(name = "otpNumber")
	private int otpNumber;

	@Column(name = "isActive")
	private int isActive;

	@Column(name = "otpDeliveryStatus")
	private int otpDeliveryStatus;

	@Column(name = "globalUserMasterId")
	private int userId;

	@Column(name = "createDateTime")
	private Date createDateTime;

	@Column(name = "expiryDateTime")
	private Date expiryDateTime;

	@Column(name = "isOTPUsed")
	private int isOTPUsed;

	@Column(name = "userOTPAttempt")
	private int userOTPAttempt;

	public OTPMaster() {
		// TODO Auto-generated constructor stub
	}

	public OTPMaster(int otpId, int applicationId, int countryCodeId, String mobileNumber, String emailId,
			int otpNumber, int isActive, int otpDeliveryStatus, int userId, Date createDateTime, Date expiryDateTime,
			int isOTPUsed, int userEnteredOTP, int userOTPAttempt) {
		super();
		this.otpId = otpId;
		this.applicationId = applicationId;
		this.countryCodeId = countryCodeId;
		this.mobileNumber = mobileNumber;
		this.emailId = emailId;
		this.otpNumber = otpNumber;
		this.isActive = isActive;
		this.otpDeliveryStatus = otpDeliveryStatus;
		this.userId = userId;
		this.createDateTime = createDateTime;
		this.expiryDateTime = expiryDateTime;
		this.isOTPUsed = isOTPUsed;
		this.userOTPAttempt = userOTPAttempt;
	}

	public int getOtpId() {
		return otpId;
	}

	public void setOtpId(int otpId) {
		this.otpId = otpId;
	}

	public int getApplicationId() {
		return applicationId;
	}

	public void setApplicationId(int applicationId) {
		this.applicationId = applicationId;
	}

	public int getCountryCodeId() {
		return countryCodeId;
	}

	public void setCountryCodeId(int countryCodeId) {
		this.countryCodeId = countryCodeId;
	}

	public String getMobileNumber() {
		return mobileNumber;
	}

	public void setMobileNumber(String mobileNumber) {
		this.mobileNumber = mobileNumber;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public int getOtpNumber() {
		return otpNumber;
	}

	public void setOtpNumber(int otpNumber) {
		this.otpNumber = otpNumber;
	}

	public int getIsActive() {
		return isActive;
	}

	public void setIsActive(int isActive) {
		this.isActive = isActive;
	}

	public int getOtpDeliveryStatus() {
		return otpDeliveryStatus;
	}

	public void setOtpDeliveryStatus(int otpDeliveryStatus) {
		this.otpDeliveryStatus = otpDeliveryStatus;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

	public Date getExpiryDateTime() {
		return expiryDateTime;
	}

	public void setExpiryDateTime(Date expiryDateTime) {
		this.expiryDateTime = expiryDateTime;
	}

	public int getIsOTPUsed() {
		return isOTPUsed;
	}

	public void setIsOTPUsed(int isOTPUsed) {
		this.isOTPUsed = isOTPUsed;
	}

	public int getUserOTPAttempt() {
		return userOTPAttempt;
	}

	public void setUserOTPAttempt(int userOTPAttempt) {
		this.userOTPAttempt = userOTPAttempt;
	}



}
