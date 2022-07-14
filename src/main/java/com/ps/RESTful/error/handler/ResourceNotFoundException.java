package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.ErrorCode;

public class ResourceNotFoundException extends BusinessException {
	private static final long serialVersionUID = 1L;

	public ResourceNotFoundException(ErrorCode errorCode, String description) {
		super(errorCode, description);
	}
}
