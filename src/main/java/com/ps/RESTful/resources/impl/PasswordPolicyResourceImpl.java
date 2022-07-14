package com.ps.RESTful.resources.impl;

import java.util.Date;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.EnumDTOMapper;
import com.ps.RESTful.dto.mapper.PasswordPolicyDTOMapper;
import com.ps.RESTful.dto.request.PasswordPolicyRequestDTO;
import com.ps.RESTful.dto.response.PasswordPolicyResponseDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.PasswordPolicyRulesEnum;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.resources.PasswordPolicyResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.dto.EnumDTO;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.PasswordPolicy;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.services.CompanyMasterService;
import com.ps.services.PasswordPolicyService;
import com.ps.util.RequestUtils;

@RestController
@RequestMapping(path = {PasswordPolicyResource.RESOURCE_PATH,PasswordPolicyResource.RESOURCE_PATH_WITH_COMPANY})
public class PasswordPolicyResourceImpl extends AbstractResourceImpl implements PasswordPolicyResource {

	Logger logger = Logger.getLogger(PasswordPolicyResourceImpl.class);
	
	@Autowired
	CompanyMasterService companyMasterService;
	
	@Autowired
	PasswordPolicyService passwordPolicyService;
	
	@Autowired
	PasswordPolicyDTOMapper passwordPolicyDTOMapper;
	
	@Autowired
	EnumDTOMapper enumDTOMapper;
	
	@Autowired
	RequestUtils requestUtils;

	@Override
	public ResponseEntity<Response> add(int companyId, PasswordPolicyRequestDTO passwordPolicyDTO) {
		
		if(logger.isDebugEnabled()) logger.debug("In Password policy add EP,  adding password policy for company id-> "+companyId);
		GlobalCompanyMaster company = companyMasterService.getById(companyId);		
		
		if(logger.isDebugEnabled()) logger.debug("Sending request to DTO mapper for mappting dto to entity");
		PasswordPolicy passwordPolicy = passwordPolicyDTOMapper.dtoToEntity(passwordPolicyDTO);
		passwordPolicy.setCompany(company);
		passwordPolicy.setCreatedBy(requestUtils.getUserName());
		
		if(logger.isDebugEnabled()) logger.debug("Sending request toservice add method for  validating the details and adding into database");
		passwordPolicyService.add(passwordPolicy, passwordPolicyDTO.getRules());
		
		if(logger.isDebugEnabled()) logger.debug("Password policy added successfully, Returning response");     
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Added password policy Successfully")
				.build());
	}

	@Override
	public ResponseEntity<Response> update(int resourceId, PasswordPolicyRequestDTO requestDTO) {
		
		if(logger.isDebugEnabled()) logger.debug("In Password policy update EP,  updating password policy with id-> "+resourceId);
		
		PasswordPolicy passwordPolicyDB = passwordPolicyService.getById(resourceId);
		
		if(logger.isDebugEnabled()) logger.debug("Sending request to DTO mapper for mappting dto to entity");
		PasswordPolicy passwordPolicyRequest = passwordPolicyDTOMapper.dtoToEntity(requestDTO);
		passwordPolicyRequest = updateRequestObject(passwordPolicyRequest, passwordPolicyDB);
		
		if(logger.isDebugEnabled()) logger.debug("Sending request toservice add method for  validating the details and adding into database");
		passwordPolicyService.update(passwordPolicyRequest, requestDTO.getRules());
		
		if(logger.isDebugEnabled()) logger.debug("Password policy added successfully, Returning response");     
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Updated password policy Successfully")
				.build());
	}
	
	@Override
	public ResponseEntity<Response> get(int resourceId){
		
		if(logger.isDebugEnabled()) logger.debug("In get password policy resource  for id-> "+resourceId);
		
		PasswordPolicy passwordPolicy = null;
		
		if(resourceId != 0) {
			passwordPolicy = passwordPolicyService.getById(resourceId);
		}else {
			
			if(logger.isDebugEnabled()) logger.debug("In get password policy resource id is 0,"
					+ " getting user from request to fetch policy for users's company");
			
			GlobalUserMaster user = requestUtils.getUser();		
			if(user == null) {
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User is Invalid!");
			}
			
			if(logger.isDebugEnabled()) logger.debug("User found in request is not null,"
					+ " calling service method to fetch policy for companyId-> "+user.getGlobalCompanyMaster().getGlobalCompanyMasterId());
			
			passwordPolicy = passwordPolicyService.getByCompanyId(user.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		}
		
		PasswordPolicyResponseDTO responseDTO = passwordPolicyDTOMapper.entityToDto(passwordPolicy);
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
				.result(responseDTO)
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Added password policy Successfully")
				.build());		
	}

	@Override
	public ResponseEntity<Response> getAllEnums() {
		
		if(logger.isDebugEnabled()) logger.debug("In getAllEnums end-point, Building response dto from enum objects");     
		List<EnumDTO> enumResponseDTOList = enumDTOMapper.enumToDTO(PasswordPolicyRulesEnum.values());
		
		return ResponseEntity
				.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
				.results(enumResponseDTOList)
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Retrieved results Successfully")
				.build());
	}
	
	private PasswordPolicy updateRequestObject(PasswordPolicy passwordPolicyRequest, PasswordPolicy passwordPolicyDB) {
		
		passwordPolicyRequest.setId(passwordPolicyDB.getId());
		passwordPolicyRequest.setCompany(passwordPolicyDB.getCompany());
		passwordPolicyRequest.setCreateDate(passwordPolicyDB.getCreateDate());
		passwordPolicyRequest.setCreatedBy(passwordPolicyDB.getCreatedBy());
		passwordPolicyRequest.setLastModifiedBy(requestUtils.getUserName());
		passwordPolicyRequest.setLastModifiedDateTime(new Date());
		
		return passwordPolicyRequest;
	}
}
