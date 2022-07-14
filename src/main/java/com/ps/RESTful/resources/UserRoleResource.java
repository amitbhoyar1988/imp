package com.ps.RESTful.resources;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.RESTful.dto.request.UserRoleRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface UserRoleResource {

	public static final String RESOURCE_PATH = "/user-role";
	public static final String ADD_ALL = "/add";
	public static final String GET_BY_GROUPNAME = "/getDetails";
	public static final String GET_BY_CompanyGroupId = "/getBycompanyGroupMasterId/{companyGroupMasterId}";
	
	
	@GetMapping(path = GET_BY_CompanyGroupId, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAllByCompanyGroupId(@PathVariable("companyGroupMasterId") Integer companyGroupMasterId);
	
	// Adding / Updating User Group 
//	@PostMapping(path = ADD_ALL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Response> addAll(@RequestBody UserGroupRequestDTO requestDTO);
	
	// Adding New User Role Details at Global Level
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> add(@RequestBody UserRoleRequestDTO requestDTO);

	// updating specific User Role Details by Role Id at Global Level
	@PutMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> update(@RequestBody UserRoleRequestDTO requestDTO);	
}
