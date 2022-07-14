package com.ps.RESTful.resources;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.RESTful.dto.request.EmployeeRoleAssingmentRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface EmployeeRoleAssingmentResource {

	public static final String RESOURCE_PATH = "/employeeRoleAssignment";
	public static final String GET_BY_GLOBALUSERMASTERID = "/user/{globalUserMasterId}";
	public static final String GET_BY_COMPANYID = "/company/{globalCompanyMasterId}";
	public static final String GET_BY_USERROLEID = "/role/{userRoleId}";
	public static final String GET_BY_USERGROUPID = "/group/{companyGroupId}";
	public static final String ADD_EMPLOYEEROLEASSIGNMENT = "/";
	public static final String UPDATE_EMPLOYEEROLEASSIGNMENT = "/";
	
	/*
	 * Fetch-All-Exist-Employee-by-userRoleId
	 */
	@GetMapping(path = GET_BY_GLOBALUSERMASTERID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAssignmentByUserId(@PathVariable("globalUserMasterId") int globalUserMasterId);
	
	/*
	 * Fetch-All-Exist-Employee-by-userRoleId
	 */
	@GetMapping(path = GET_BY_USERROLEID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getEmployeeforRoleID(@PathVariable("userRoleId") int userRoleId);

	/*
	 * Fetch-All-Exist-Employee-by-CompanyId
	 */
	@GetMapping(path = GET_BY_COMPANYID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getEmployeeforCompanyId(
			@PathVariable("globalCompanyMasterId") int globalCompanyMasterId);

	/*
	 * Fetch-All-Exist-Employee-by-userGroupId
	 */
	@GetMapping(path = GET_BY_USERGROUPID, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getEmployeeforCompanyGroupId(@PathVariable("companyGroupId") int companyGroupId);

	/*
	 * Add-New-EmployeeRoleAssginment-Details
	 */
	@PostMapping(path = ADD_EMPLOYEEROLEASSIGNMENT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addNewEmployeeRoleAssignment(@RequestBody List<EmployeeRoleAssingmentRequestDTO> requestDTO);

	/*
	 * Update-Existing-EmployeeRoleAssginment-Details
	 */
	@PutMapping(path = UPDATE_EMPLOYEEROLEASSIGNMENT, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> updateEmployeeRoleAssignment(@RequestBody List<EmployeeRoleAssingmentRequestDTO> requestDTO);

}
