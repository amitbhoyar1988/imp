package com.ps.dto;

import java.util.List;

public class PasswordPolicyDTO extends AbstractTimeDTO {

	private List<EnumDTO> rules;
		
	private int minLength;
	
	private int maxLength;
	
	private int repeatCharsCount;
	
	private String allowedSetOfSpecialChars;
	
	private String illegalSetOfChars;
	
	private boolean isExpired;
	
	private int expiryInDays;
	
	private String description;	
	
	private int allowedPasswordHistorycount = 3;
	
	private int dueInDays;

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

	public boolean isExpired() {
		return isExpired;
	}

	public void setExpired(boolean isExpired) {
		this.isExpired = isExpired;
	}

	public int getExpiryInDays() {
		return expiryInDays;
	}

	public void setExpiryInDays(int expiryInDays) {
		this.expiryInDays = expiryInDays;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<EnumDTO> getRules() {
		return rules;
	}

	public void setRules(List<EnumDTO> rules) {
		this.rules = rules;
	}

	public int getRepeatCharsCount() {
		return repeatCharsCount;
	}

	public void setRepeatCharsCount(int repeatCharsCount) {
		this.repeatCharsCount = repeatCharsCount;
	}

	public String getAllowedSetOfSpecialChars() {
		return allowedSetOfSpecialChars;
	}

	public void setAllowedSetOfSpecialChars(String allowedSetOfSpecialChars) {
		this.allowedSetOfSpecialChars = allowedSetOfSpecialChars;
	}

	public String getIllegalSetOfChars() {
		return illegalSetOfChars;
	}

	public void setIllegalSetOfChars(String illegalSetOfChars) {
		this.illegalSetOfChars = illegalSetOfChars;
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
