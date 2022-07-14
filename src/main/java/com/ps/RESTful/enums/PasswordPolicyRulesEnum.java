package com.ps.RESTful.enums;

public enum PasswordPolicyRulesEnum {

	NONE("No Password policy",0),
	ONE_DIGIT_RULE("Password should be Alpha-Numeric",1),
	ONE_UPPERCASE_RULE("Password should contain Uppercase letters",2),
	ONE_LOWERCASE_RULE("Password should contain Lowercase letters",4),
	NO_FIRSTNAME_RULE("Password Should not contain Users' First name",8),
	NO_LASTNAME_RULE("Password Should not contain Users' Last name",16),
	SPECIAL_CHARS_RULE("Password should contain one Special character",32),
	ILLEGAL_CHARS_RULE("Password should not contain %s characters",64),
	NO_WHITESPACE_RULE("Password should not contain White space",128),
	REPEAT_CHARS_RULE("Password should not contain %d or more Consecutive identical characters",256);
	
	private String value;
	private long bitValue;
	
	private PasswordPolicyRulesEnum(String value,long bitValue) {
		this.value = value;
		this.bitValue = bitValue;
	}

	public static boolean isValid(String name) {			
		boolean isValid = false;
		
		for (PasswordPolicyRulesEnum rule: PasswordPolicyRulesEnum.values()) {
			
			if(rule.name().equals(name)) {
				isValid = true;
				break;
			}
		}			
		return isValid;
	}
	
	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public long getBitValue() {
		return bitValue;
	}

	public void setBitValue(long bitValue) {
		this.bitValue = bitValue;
	}
}
