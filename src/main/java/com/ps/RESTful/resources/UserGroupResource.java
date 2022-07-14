package com.ps.RESTful.resources;

import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.ps.RESTful.dto.request.UserGroupReportRequestDTO;
import com.ps.RESTful.dto.request.UserGroupRequestDTO;
import com.ps.RESTful.resources.response.handler.Response;

public interface UserGroupResource {

	public static final String RESOURCE_PATH = "/user-group";
	public static final String GET_ALL_Roles = "/user-role";
	public static final String ADD_ALL = "/add";
	public static final String GET_BY_GROUPNAME = "/getDetails";
	public static final String GET_BY_CompanyGroupId = "/getByCompanyGroupId/{CompanyGroupId}";
	public static final String GET_ALL_Distinct = "/getAllDistinctUserGroups";
	public static final String GET_ALL_Distinct_UserGroups = "/getAllDistinctByCompanyGroups";
	public static final String GET_ALL_CompanyGroups = "/getAllCompanyGroupsByUserGroup";
	public static final String GET_Assigned_CompanyGroups = "/getAssignedCompanyGroupsByUserGroupName";
	public static final String GET_ALL_UserGroup_BY_CompanyGroups = "/getAllUserGroupsByCompanyGroups";
		
	@PostMapping(path = GET_ALL_UserGroup_BY_CompanyGroups, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAllUserGroupsByCompanyGroupIds(@RequestBody UserGroupReportRequestDTO requestDTO);
	
	@GetMapping(path = GET_BY_CompanyGroupId, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response>getAllUserGroupsByCompanyGroupId(@PathVariable("CompanyGroupId") Integer CompanyGroupId);
		
	@GetMapping(path = GET_ALL_Distinct, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAllDistinctUserGroups();
	
	@PostMapping(path = GET_ALL_Distinct_UserGroups, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAllDistinctByCompanyGroups(@RequestBody UserGroupReportRequestDTO requestDTO);
	
	@PostMapping(path = GET_ALL_CompanyGroups, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAllCompanyGroupByUserGroups(@RequestBody UserGroupReportRequestDTO requestDTO);
	
	@PostMapping(path = GET_Assigned_CompanyGroups, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getAssignedCompanyGroupByUserGroupName(@RequestBody UserGroupReportRequestDTO requestDTO);
		
//	@GetMapping(path = GET_BY_CompanyGroupId, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
//	public ResponseEntity<Response> getByCompanyGroupId(@PathVariable("CompanyGroupId") Integer CompanyGroupId);
	
	@PostMapping(path = GET_BY_GROUPNAME, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> getUserGroupDetails(@RequestBody UserGroupReportRequestDTO requestDTO);
	
//	// Adding New CompanyGroup in CompanyGroupMaster
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> add(@RequestBody UserGroupRequestDTO requestDTO);
	
	// Adding / Updating User Group 
	@PostMapping(path = ADD_ALL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Response> addAll(@RequestBody UserGroupRequestDTO requestDTO);
}
