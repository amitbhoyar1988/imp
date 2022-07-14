package com.ps.RESTful.resources.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.UserGroupDTOMapper;
import com.ps.RESTful.dto.mapper.UserRoleDTOMapper;
import com.ps.RESTful.dto.request.UserGroupReportRequestDTO;
import com.ps.RESTful.dto.request.UserGroupRequestDTO;
import com.ps.RESTful.dto.response.UserGroupResponseDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.resources.UserGroupResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.beans.UserGroupBean;
import com.ps.beans.UserGroupRequestBean;
import com.ps.dto.ErrorDTO;
import com.ps.dto.UserGroupDTO;
import com.ps.entities.master.CompanyGroupMasterBean;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupReport;
import com.ps.services.UserGroupService;
import com.ps.util.MethodValidationUtils;
import com.ps.util.StringUtils;

@RestController
@RequestMapping(path = UserGroupResource.RESOURCE_PATH)
public class UserGroupResourceImpl implements UserGroupResource {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	UserGroupDTOMapper dtoMapper;
	
	@Autowired
	UserRoleDTOMapper userRoleDTOMapper;

	@Autowired
	UserGroupService userGroupService;

	// GetAll All User Groups
	@Override
	public ResponseEntity<Response> getAllUserGroupsByCompanyGroupIds(UserGroupReportRequestDTO requestDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In User Group resource getAllUserGroups by Company Group Ids mtd");

		List<UserGroup> groupList = new ArrayList<>();
		
		if (requestDTO.getListCompanyGroupIds().isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
		}

		// Calling Implemented Service method
		groupList = userGroupService.getAllUserGroupsByCompanyGroupIds(requestDTO.getListCompanyGroupIds());

		if (groupList != null && !groupList.isEmpty()) {
			List<UserGroupResponseDTO> responseDtoList = dtoMapper.entityListToDtoList(groupList);

			if (logger.isDebugEnabled())
				logger.debug("Returning response from UserGroup resource for all UserGroups ");

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									+responseDtoList.size() + ": User Groups list found")
							.results(responseDtoList).build());
		} else {
			logger.error("User Groups list is empty");
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
							SuccessCode.NO_CONTENT.getCode(), ": User Groups list is empty").results(groupList)
							.build());
		}
	}
	
	// GetAll All User Groups
	@Override
	public ResponseEntity<Response> getAllDistinctUserGroups() {

		if (logger.isDebugEnabled())
			logger.debug("In User Group resource getAllDistinctUserGroups mtd");

		List<UserGroupReport> groupList = new ArrayList<>();

		// Calling Implemented Service method
		groupList = userGroupService.getAllDistinctUserGroups();

		if (groupList != null && !groupList.isEmpty()) {
			List<UserGroupResponseDTO> responseDtoList = dtoMapper.entityListToDtoListNew(groupList);

			if (logger.isDebugEnabled())
				logger.debug("Returning response from UserGroup resource for all Distinct UserGroups ");

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									+responseDtoList.size() + ": User Groups list found")
							.results(responseDtoList).build());
		} else {
			logger.error("User Groups list is empty");
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
							SuccessCode.NO_CONTENT.getCode(), ": User Groups list is empty").results(groupList)
							.build());
		}
	}
	
	@Override
	public ResponseEntity<Response> getAllCompanyGroupByUserGroups(UserGroupReportRequestDTO requestDTO) {
		if (logger.isDebugEnabled())
			logger.debug("In User Group resource getAllCompanyGroupByUserGroups mtd");

		// Check CompanyGroupId input valid or not
		if (!StringUtils.isValidString(requestDTO.getUserGroupName())) {
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Provided groupName is Invalid");
		}
				
		if (logger.isDebugEnabled())
			logger.debug("Request for getting Company Group List  for UserGroup-> " + requestDTO.getUserGroupName());

		List<CompanyGroupMasterBean> list = new ArrayList<>();
		
		if (requestDTO.getListCompanyGroupIds().isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
		}
		
		list = userGroupService.getAllCompanyGroupByUserGroups(requestDTO.getUserGroupName(), requestDTO.getListCompanyGroupIds());

		if (list != null && !list.isEmpty()) {
			// Converting_entity_to_DTO
//			List<UserGroupResponseDTO> responseDtoList = dtoMapper.entityListToDtoList(list);

			if (logger.isDebugEnabled()) {
				logger.debug("Company Group details list retrieved successfully for User Group-> " + requestDTO.getUserGroupName());
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									"Company Group details list retrieved successfully")
							.results(list).build());
		} else {
			logger.error("Company Group list is empty");
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
							SuccessCode.NO_CONTENT.getCode(), "Company Group details list is empty")
							.results(list).build());
		}
	}
	
	
	@Override
	public ResponseEntity<Response> getAssignedCompanyGroupByUserGroupName(UserGroupReportRequestDTO requestDTO) {
		if (logger.isDebugEnabled())
			logger.debug("In User Group resource getAssignedCompanyGroupByUserGroupName mtd");

		// Check CompanyGroupId input valid or not
		if (!StringUtils.isValidString(requestDTO.getUserGroupName())) {
			if (logger.isDebugEnabled())
				logger.debug("Provide the User Group Name-> " + requestDTO.getUserGroupName());
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Provide the User Group Name");
		}
				
		if (logger.isDebugEnabled())
			logger.debug("Request for getting Company Group List  for UserGroup-> " + requestDTO.getUserGroupName());

		List<UserGroupBean> list = new ArrayList<>();		
		
		MethodValidationUtils.checkIfDatabaseObjectListIsNotEmpty(requestDTO.getListCompanyGroupIds(), "Company Group Id");
//		if (requestDTO.getListCompanyGroupIds().isEmpty()) {
//			if (logger.isDebugEnabled())
//				logger.debug("Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
//			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
//		}
		
		list = userGroupService.getAllCompanyGroupByUserGroupName(requestDTO.getUserGroupName(), requestDTO.getListCompanyGroupIds());

		if (list != null && !list.isEmpty()) {
			// Converting_entity_to_DTO
//			List<UserGroupResponseDTO> responseDtoList = dtoMapper.entityListToDtoList(list);

			if (logger.isDebugEnabled()) {
				logger.debug("Company Group details list retrieved successfully for User Group-> " + requestDTO.getUserGroupName());
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									"Company Group details list retrieved successfully")
							.results(list).build());
		} else {
			logger.error("Company Group list is empty");
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
							SuccessCode.NO_CONTENT.getCode(), "Company Group details list is empty")
							.results(list).build());
		}
	}
	
	@Override
	public ResponseEntity<Response> getAllDistinctByCompanyGroups(UserGroupReportRequestDTO requestDTO) {
		if (logger.isDebugEnabled())
			logger.debug("In User Group resource getAllDistinctByCompanyGroups mtd");
				
		if (logger.isDebugEnabled())
			logger.debug("Request for getting User Group List ");

		List<UserGroupBean> list = new ArrayList<>();		
		
		MethodValidationUtils.checkIfDatabaseObjectListIsNotEmpty(requestDTO.getListCompanyGroupIds(), "Company Group Id");
//		if (requestDTO.getListCompanyGroupIds().isEmpty()) {
//			if (logger.isDebugEnabled())
//				logger.debug("Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
//			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Please provide the list of Company Group Ids:" + requestDTO.getListCompanyGroupIds());
//		}
		
		list = userGroupService.getAllDistinctUserGroups(requestDTO.getListCompanyGroupIds());

		if (list != null && !list.isEmpty()) {
			// Converting_entity_to_DTO
//			List<UserGroupResponseDTO> responseDtoList = dtoMapper.entityListToDtoList(list);

			if (logger.isDebugEnabled()) {
				logger.debug("User Group list retrieved successfully");
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									"User Group list retrieved successfully")
							.results(list).build());
		} else {
			logger.error("User Group list is empty");
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
							SuccessCode.NO_CONTENT.getCode(), "User Group list is empty")
							.results(list).build());
		}
	}
	
	
	@Override
	public ResponseEntity<Response> getAllUserGroupsByCompanyGroupId(Integer CompanyGroupId) {
		if (logger.isDebugEnabled())
			logger.debug("In User Group resource getByCompanyGroupId");

		// Check CompanyGroupId input valid or not
		if (CompanyGroupId == 0) {
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Provided CompanyGroupId is Invalid");
		}
				
		if (logger.isDebugEnabled())
			logger.debug("Request for getting User Group List  for CompanyGroupId-> " + CompanyGroupId);

		List<UserGroupBean> list = new ArrayList<>();

		list = userGroupService.getAllUserGroupsByCompanyGroupId(CompanyGroupId);
		List<UserGroupDTO> responseDtoList = new ArrayList<>();

		if (list != null && !list.isEmpty()) {
			// Converting_entity_to_DTO
			responseDtoList = dtoMapper.beanListToDtoList(list);

			if (logger.isDebugEnabled()) {
				logger.debug("User Group details list retrieved successfully for CompanyGroupId-> " + CompanyGroupId);
			}

			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
									"User Group list retrieved successfully")
							.results(responseDtoList).build());
		} else {
			logger.error("User Group list is empty");
			return ResponseEntity.status(HttpStatus.OK)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
							SuccessCode.NO_CONTENT.getCode(), "User Group list is empty")
							.results(responseDtoList).build());
		}
	}

//	// GetAll All User Groups
//	@Override
//	public ResponseEntity<Response> getAllUserRoles() {
//
//		if (logger.isDebugEnabled())
//			logger.debug("In User Group resource getAllUserGroups mtd");
//
//		List<UserRole> groupList = new ArrayList<>();
//
//		// Calling Implemented Service method
//		groupList = userGroupService.getAllUserRole();
//
//		if (groupList != null && !groupList.isEmpty()) {
//			List<UserRoleResponseDTO> responseDtoList = userRoleDTOMapper.entityListToDtoList(groupList);
//
//			if (logger.isDebugEnabled())
//				logger.debug("Returning response from UserGroup resource for all UserGroupss ");
//
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(ResponseBuilder.builder()
//							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
//									+responseDtoList.size() + ": User Groups list found")
//							.results(responseDtoList).build());
//		} else {
//			logger.error("User Groups list is empty");
//			return ResponseEntity.status(HttpStatus.OK)
//					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(),
//							SuccessCode.NO_CONTENT.getCode(), ": User Groups list is empty").results(groupList)
//							.build());
//		}
//	}
		
	@Override
	public ResponseEntity<Response> add(UserGroupRequestDTO requestDTO) {
		if (logger.isDebugEnabled())
			logger.debug("Adding New User Group Details: " + requestDTO);

		// validating data while adding new records
		ErrorDTO error = validateUserGroupFields(requestDTO);
		MethodValidationUtils.errorValidation(error);

		List<UserGroup> userGroupList = dtoMapper.dtoToEntity11(requestDTO);

		if (logger.isDebugEnabled())
			logger.debug("Sending User Group Details entity to Service method for saving into db " + userGroupList);

		// Calling Implemented Service method
		userGroupList = userGroupService.addAllUserGroup(userGroupList);

		// converting entity to DTO
		List<UserGroupResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(userGroupList);

		if (logger.isDebugEnabled())
			logger.debug("User Group added, Returning response from User Group resource for " + userGroupList);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(), "User Group added Successfully")
				.result(responseDTOList).build());
	}
		
		@Override
		public ResponseEntity<Response> addAll(UserGroupRequestDTO requestDTO) {
//			 if (logger.isDebugEnabled())
//			      logger.debug("Adding New User Group Details: " + requestDTO);

			UserGroupRequestBean userGroupBean = new UserGroupRequestBean();
			userGroupBean = dtoMapper.dtoToBean(requestDTO);

//			strCompanyGroupIds = StringUtils.join(requestDTO.getGroupIdList().toArray(), ",");

//			 		 
//			 List<UserGroup> userGroupList = dtoMapper.dtoListToEntityList(requestDTO);
//			List<UserGroup> userGroupList = new ArrayList<>();

//			if (logger.isDebugEnabled())
//			   logger.debug("Sending User Group Details entity to Service method for saving into db " + userGroupList);

			// Calling Implemented Service method
			String result = userGroupService.addAll(userGroupBean);

			// converting entity to DTO
//			List<UserGroupResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(userGroupList);
//			
//			if (logger.isDebugEnabled())
//				logger.debug("User Group added, Returning response from User Group resource for " + userGroupList);

			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(),
							"User Group added Successfully").result(result).build());
		}
		
		
		@Override
		public ResponseEntity<Response> getUserGroupDetails(UserGroupReportRequestDTO requestDTO) {
			if (logger.isDebugEnabled())
				logger.debug("In User Group resource getUserGroupDetails");

			if (logger.isDebugEnabled())
				logger.debug("Request for getting User Group Details for groupName-> " + requestDTO.getUserGroupName());

//			// Check Category name input valid or not
//			if (!StringUtils.isValidString(Category)) {
//				throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Provided Category is Invalid");
//			}

			UserGroupRequestBean result  = new UserGroupRequestBean();

			result = userGroupService.getUserGroupDetails(requestDTO.getUserGroupName());
			
			  // Checking return object is null or not
		    MethodValidationUtils.checkIfObjectIsNotNULL(result, UserGroup.class.getSimpleName());

			if (result != null && !result.getCompanyGroupIds().isEmpty()) {
//				// Converting_entity_to_DTO
//				List<CompanyGroupDropDownlistMasterResponseDTO> responseDTOList = dtoMapper.enityListToDtoList(ddlList);
//
//				if (logger.isDebugEnabled()) {
//					logger.debug("CompanyGroup DropDownlist Master retrieved successfully for Category-> " + Category);
//				}

				return ResponseEntity.status(HttpStatus.OK)
						.body(ResponseBuilder.builder()
								.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
										"User Group Details retrieved successfully")
								.result(result).build());
			} else {
				 return ResponseEntity.status(HttpStatus.OK)
					        .body(ResponseBuilder.builder()
					            .status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "User Group Details not found")
					            .result(result).build());
			}
		}
		
		
		// validating User Group Data fields
		private ErrorDTO validateUserGroupFields(UserGroupRequestDTO dto) {

			// validating Company Group list
			if (dto.getCompanyGroupMasterIds().isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Company Group list not provided ");
				return new ErrorDTO(ErrorCode.INVALID_PARAMETER, "Company Group list not provided ");
			}

			// validating GroupName
			if (!StringUtils.isValidString(dto.getGroupName())) {
				if (logger.isDebugEnabled())
					logger.debug("User Group Name not provided ");
				return new ErrorDTO(ErrorCode.INVALID_PARAMETER, "User Group Name not provided ");
			}

			// validating GroupDescription
			if (!StringUtils.isValidString(dto.getGroupDescription())) {
				if (logger.isDebugEnabled())
					logger.debug("User Group Description not provided ");
				return new ErrorDTO(ErrorCode.INVALID_PARAMETER, "User Group Description not provided ");
			}

			// Remark validation
			if (!dto.isActive()) {
				if (!StringUtils.isValidString(dto.getRemark())) {
					if (logger.isDebugEnabled())
						logger.debug("Remark is mandatory while De-Activating the User Group");
					return new ErrorDTO(ErrorCode.INVALID_PARAMETER,
							"Remark is mandatory while De-Activating the User Group");
				}
			}
			return null;
		}
	
}
