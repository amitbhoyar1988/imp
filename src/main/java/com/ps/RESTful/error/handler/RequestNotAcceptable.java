package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.ErrorCode;

public class RequestNotAcceptable extends BusinessException {

	private static final long serialVersionUID = 1L;

	public RequestNotAcceptable(ErrorCode errorCode, String description) {
		super(errorCode, description);
	}
	
}
