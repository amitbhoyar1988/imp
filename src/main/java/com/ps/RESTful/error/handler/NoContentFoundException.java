package com.ps.RESTful.error.handler;

import com.ps.RESTful.enums.SuccessCode;

public class NoContentFoundException extends BusinessException {

	private static final long serialVersionUID = 1L;

	public NoContentFoundException(SuccessCode successCode, String description) {
		super(successCode, description);
	}
}
