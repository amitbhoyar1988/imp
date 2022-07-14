package com.ps.RESTful.resources;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.RESTful.dto.request.OTPDetailsRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface OTPDetailsResource {
	
	public static final String RESOURCE_PATH = "/otp";
	public static final String RESOURCE_PATH_VALIDATE = "/validate";
	public static final String RESOURCE_PATH_RESEND = "/resend";

	@PostMapping( path =RESOURCE_PATH_VALIDATE ,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> validate(@RequestBody OTPDetailsRequestDTO requestDTO);
	
	@PostMapping( path =RESOURCE_PATH_RESEND ,
			consumes = MediaType.APPLICATION_JSON_VALUE, 
			produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> resend(@RequestBody OTPDetailsRequestDTO requestDTO);
	

}
