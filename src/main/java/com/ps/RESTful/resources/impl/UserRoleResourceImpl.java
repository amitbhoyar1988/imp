package com.ps.RESTful.resources.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.UserRoleDTOMapper;
import com.ps.RESTful.dto.request.UserRoleRequestDTO;
import com.ps.RESTful.dto.response.UserRoleResponseDTO;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.resources.UserRoleResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.entities.master.UserRole;
import com.ps.services.UserRoleService;
import com.ps.util.MethodValidationUtils;


@RestController
@RequestMapping(path = UserRoleResource.RESOURCE_PATH)
public class UserRoleResourceImpl implements UserRoleResource {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	UserRoleDTOMapper userRoleDTOMapper;

	@Autowired
	UserRoleService userRoleService;

	// GetAll All User Groups
	@Override
	public ResponseEntity<Response> getAllByCompanyGroupId(Integer companyGroupMasterId) {

		if (logger.isDebugEnabled())
			logger.debug("In User Role resource getAll mtd");

		List<UserRole> rolesList = new ArrayList<>();

		// Calling Implemented Service method
		rolesList = userRoleService.getAll(companyGroupMasterId);

		if (rolesList != null && !rolesList.isEmpty()) {
			List<UserRoleResponseDTO> responseDtoList = userRoleDTOMapper.entityListToDtoList(rolesList);

			if (logger.isDebugEnabled())
				logger.debug("Returning response from User Role resource for all User Roles ");

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									+responseDtoList.size() + ": User Roles list found")
							.results(responseDtoList).build());
		} else {
			logger.error("User Roles list is empty");
			return ResponseEntity.status(HttpStatus.OK).body(ResponseBuilder.builder()
					.status(StatusEnum.SUCCESS.getValue(), SuccessCode.NO_CONTENT.getCode(), "User Roles list is empty")
					.results(rolesList).build());
		}
	}
	
	  @Override
	  public ResponseEntity<Response> add(UserRoleRequestDTO requestDTO) 
	  {
		 if (logger.isDebugEnabled())
		     logger.debug("Adding New Company Registration Details: " + requestDTO);
			 
		 UserRole roleDetails = userRoleDTOMapper.dtoToEntity(requestDTO);
			
		if (logger.isDebugEnabled())
		    logger.debug("Sending User Role Details entity to Service method for saving into db "
			          + roleDetails);
			
			// Calling Implemented Service method
		roleDetails = userRoleService.add(roleDetails, requestDTO.getCompanyGroupMasterId());
			
		// converting entity to DTO
		UserRoleResponseDTO responseDTO = userRoleDTOMapper.entityToDto(roleDetails);
			
		if (logger.isDebugEnabled())
			logger.debug("User Role Details saved, Returning response from UserRole resource for " + roleDetails);
					
		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder()
			.status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(), "User Role Details saved Successfully")
			.result(responseDTO).build());
		}
	  
	  @Override
		public ResponseEntity<Response> update(UserRoleRequestDTO requestDTO) 
	   {			
		  if (logger.isDebugEnabled())
			 logger.debug("Modifying User Role Details for UserRoleId: " + requestDTO.getUserRoleId());
			
			// Check_Id_is_Not_Null/Zero
		    MethodValidationUtils.checkIfIdIsZero(requestDTO.getUserRoleId(), "UserRoleId");
		    
			if (logger.isDebugEnabled())
				logger.debug("Mapping dto to entity");
			UserRole roleData = userRoleDTOMapper.dtoToEntity(requestDTO);
			
			if (logger.isDebugEnabled())
				logger.debug("calling service mtd in User Role resource: " + roleData);
			
			// implementing service method for update 
			roleData = userRoleService.update(roleData, requestDTO.getCompanyGroupMasterId());
			
			// converting entity to DTO  to returning response
			UserRoleResponseDTO responseDTO = userRoleDTOMapper.entityToDto(roleData);
			
			if (logger.isDebugEnabled())
				logger.debug("User Role Details updated, Returning response from User Role resource for " + roleData);
			return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder()
					.status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(), "Updated User Role Details Successfully")
					.result(responseDTO).build());
		}
}
