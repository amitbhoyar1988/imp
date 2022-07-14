package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.ErrorCode;

public class InvalidRequestException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public InvalidRequestException(ErrorCode errorCode, String description) {
		super(errorCode, description);
	}
}
