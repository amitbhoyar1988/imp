package com.ps.RESTful.resources;


import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.RESTful.dto.request.PasswordPolicyRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface PasswordPolicyResource  {

	public static final String RESOURCE_PATH = "/password-policy";
	public static final String RESOURCE_PATH_WITH_COMPANY = "/company/{companyId}/password-policy";
	public static final String RESOURCE_ID_REQUEST_PATH = "/{resourceId}";
	public static final String ENUM_REQUEST_PATH = "/enums";
	
	@PostMapping(
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Response> add(@PathVariable("companyId") int companyId,
			@RequestBody PasswordPolicyRequestDTO requestDTO);
	
	@PutMapping(
			path = RESOURCE_ID_REQUEST_PATH,
			consumes = MediaType.APPLICATION_JSON_VALUE,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Response> update(@PathVariable("resourceId") int resourceId, @RequestBody PasswordPolicyRequestDTO requestDTO);
	
	@GetMapping(
			path = RESOURCE_ID_REQUEST_PATH,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Response> get(@PathVariable("resourceId") int resourceId);
	
	@GetMapping(
			path = ENUM_REQUEST_PATH,
			produces = MediaType.APPLICATION_JSON_VALUE
			)
	public ResponseEntity<Response> getAllEnums();
	
	

}
