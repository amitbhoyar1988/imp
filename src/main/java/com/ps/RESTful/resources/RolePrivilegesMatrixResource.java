package com.ps.RESTful.resources;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.RESTful.dto.request.RolePrivilegesMatrixRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface RolePrivilegesMatrixResource {

	public static final String RESOURCE_PATH = "/userRolePrivilegesMatrix";
	public static final String GET_MENUES_BY_ROLEID = "/{userRoleId}";
	public static final String ADD_PRIVILEGES = "/";
	public static final String UPDATE_PRIVILEGES = "/";
	
	
	/*
	 * Fetch-All-Exist-Privileges-by-userRoleId
	 */
	@GetMapping(path = GET_MENUES_BY_ROLEID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getMenuesforRoleID(@PathVariable("userRoleId") int userRoleId);
	
	/*
	 * ADD-New-Privileges
	 */
	@PostMapping(path = ADD_PRIVILEGES,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addPrivileges(@RequestBody List<RolePrivilegesMatrixRequestDTO> requestDTOList);

	/*
	 * Update-Exist-Privileges
	 */
	@PutMapping(path = UPDATE_PRIVILEGES,consumes = MediaType.APPLICATION_JSON_VALUE,produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> updatePrivileges(@RequestBody List<RolePrivilegesMatrixRequestDTO> requestDTOList);
	
}