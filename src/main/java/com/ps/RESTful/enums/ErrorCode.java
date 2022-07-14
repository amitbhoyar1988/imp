package com.ps.RESTful.enums;

public enum ErrorCode {

	// 400--> BAD_REQUEST FAMILY CODE
	BAD_REQUEST("400"), INVALID_PARAMETER("4001"), REDIRECT_REQUEST("4002"),

	// 401--> UNAUTHORIZED FAMILY CODE
	UNAUTHORIZED("401"), CLIENT_NOT_ACCESSIBLE("4011"),

	// 404--> NOT_FOUND FAMILTY CODE
	RESOURCE_NOT_FOUND("404"),

	// 406--> NOT ACCEPTABLE
	NOT_ACCEPTABLE("406"),

	// 409--> Entry Already Exist
	ALREADY_EXIST("409"),

	// 500--> INTERNAL SERVER ERROR FAMILTY CODE
	INTERNAL_SERVER_ERROR("500");

	private String code;

	private ErrorCode(String code) {
		this.code = code;
	}

	public String getCode() {
		return code;
	}
}
