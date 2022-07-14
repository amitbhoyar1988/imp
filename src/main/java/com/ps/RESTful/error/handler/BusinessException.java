package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.SuccessCode;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 1L;
	
	protected String description;
	protected ErrorCode errorcode;
	protected SuccessCode successCode;
	
	public BusinessException(ErrorCode errorCode,String description) {
		super(errorCode.name());
		this.description = description;
		this.errorcode = errorCode;
	}
	
	public BusinessException(SuccessCode successCode, String description) {
		super(successCode.name());
		this.description = description;
		this.successCode = successCode;
	}

	
	public ErrorCode getErrorCode() {
		return errorcode;
	}
	
	public String getDescription() {
		return description;
	}
}
