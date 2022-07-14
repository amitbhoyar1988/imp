package com.ps.entities.master;


import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "OtpFailedTransaction")
public class OtpFailedTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "otpFailedTransactionId")
	private int otpFailedTransactionId;

	@Column(name = "otpTransactionMasterId")
	private int otpId;

	@Column(name = "userEnteredOTP")
	private int userEnteredOTP;

	@Column(name = "createDateTime")
	private Date createDateTime;

	public OtpFailedTransaction() {
		// TODO Auto-generated constructor stub
	}

	public OtpFailedTransaction(int otpFailedTransactionId, int otpId, int userEnteredOTP, Date createDateTime) {
		super();
		this.otpFailedTransactionId = otpFailedTransactionId;
		this.otpId = otpId;
		this.userEnteredOTP = userEnteredOTP;
		this.createDateTime = createDateTime;
	}

	public int getOtpFailedTransactionId() {
		return otpFailedTransactionId;
	}

	public void setOtpFailedTransactionId(int otpFailedTransactionId) {
		this.otpFailedTransactionId = otpFailedTransactionId;
	}

	public int getOtpId() {
		return otpId;
	}

	public void setOtpId(int otpId) {
		this.otpId = otpId;
	}

	public int getUserEnteredOTP() {
		return userEnteredOTP;
	}

	public void setUserEnteredOTP(int userEnteredOTP) {
		this.userEnteredOTP = userEnteredOTP;
	}

	public Date getCreateDateTime() {
		return createDateTime;
	}

	public void setCreateDateTime(Date createDateTime) {
		this.createDateTime = createDateTime;
	}

}
