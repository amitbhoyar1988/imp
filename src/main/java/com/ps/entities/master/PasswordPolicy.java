package com.ps.entities.master;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;

@Entity
public class PasswordPolicy extends AbstractTimeEntity {

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="passwordPolicyId")
	private int id;
	
	@OneToOne
	@JoinColumn(name="companyId",referencedColumnName = "globalCompanyMasterId")
	private GlobalCompanyMaster company;
	
	private String name;
	
	private long rulesBitMap;
	
	private int minLength = 8;
	
	private int maxLength = 16;
	
	private int repeatCharsCount;
	
	private String allowedSetOfSpecialChars;
	
	private String illegalSetOfChars;
	
	@Column(columnDefinition = "int default 0")
	private int allowedPasswordHistorycount;
		
	private String description;
	
	private int expiryInDays;
	
	private boolean isExpired = false;
	
	@Column(columnDefinition = "int default 0")
	private int dueInDays;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public GlobalCompanyMaster getCompany() {
		return company;
	}

	public void setCompany(GlobalCompanyMaster company) {
		this.company = company;
	}

	public long getRulesBitMap() {
		return rulesBitMap;
	}

	public void setRulesBitMap(long rulesBitMap) {
		this.rulesBitMap = rulesBitMap;
	}

	public int getMinLength() {
		return minLength;
	}

	public void setMinLength(int minLength) {
		this.minLength = minLength;
	}

	public int getMaxLength() {
		return maxLength;
	}

	public void setMaxLength(int maxLength) {
		this.maxLength = maxLength;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getExpiryInDays() {
		return expiryInDays;
	}

	public void setExpiryInDays(int expiryInDays) {
		this.expiryInDays = expiryInDays;
	}

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getRepeatCharsCount() {
		return repeatCharsCount;
	}

	public void setRepeatCharsCount(int repeatCharsCount) {
		this.repeatCharsCount = repeatCharsCount;
	}

	public String getIllegalSetOfChars() {
		return illegalSetOfChars;
	}

	public void setIllegalSetOfChars(String illegalSetOfChars) {
		this.illegalSetOfChars = illegalSetOfChars;
	}

	public String getAllowedSetOfSpecialChars() {
		return allowedSetOfSpecialChars;
	}

	public void setAllowedSetOfSpecialChars(String allowedSetOfSpecialChars) {
		this.allowedSetOfSpecialChars = allowedSetOfSpecialChars;
	}

	public int getAllowedPasswordHistorycount() {
		return allowedPasswordHistorycount;
	}

	public void setAllowedPasswordHistorycount(int allowedPasswordHistorycount) {
		this.allowedPasswordHistorycount = allowedPasswordHistorycount;
	}

	public int getDueInDays() {
		return dueInDays;
	}

	public void setDueInDays(int dueInDays) {
		this.dueInDays = dueInDays;
	}

}
