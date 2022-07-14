package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.ErrorCode;

public class ResourceAlreadyExistException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public ResourceAlreadyExistException(ErrorCode errorCode, String description) {
		// TODO Auto-generated constructor stub
		super(errorCode, description);
	}
}
