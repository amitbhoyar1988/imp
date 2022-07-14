package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.ErrorCode;

public class InternalServerErrorException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public InternalServerErrorException(ErrorCode errorCode, String description) {
		super(errorCode, description);
	}
}
