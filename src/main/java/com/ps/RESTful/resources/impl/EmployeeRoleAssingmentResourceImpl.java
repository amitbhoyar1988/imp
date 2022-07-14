package com.ps.RESTful.resources.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.EmployeeRoleAssingmentDTOMapper;
import com.ps.RESTful.dto.request.EmployeeRoleAssingmentRequestDTO;
import com.ps.RESTful.dto.response.EmployeeRoleAssingmentResponseDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.resources.EmployeeRoleAssingmentResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.entities.master.EmployeeRoleAssingment;
import com.ps.services.EmployeeRoleAssingmentService;
import com.ps.util.MethodValidationUtils;
import com.ps.util.StringUtils;

@RestController
@RequestMapping(path = EmployeeRoleAssingmentResource.RESOURCE_PATH)
public class EmployeeRoleAssingmentResourceImpl implements EmployeeRoleAssingmentResource {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	EmployeeRoleAssingmentService emplRoleAssignmentService;

	@Autowired
	EmployeeRoleAssingmentDTOMapper dtoMapper;

	@Override
	public ResponseEntity<Response> getEmployeeforRoleID(int userRoleId) {

		if (logger.isDebugEnabled())
			logger.debug("In getEmployeeforRoleID Method from EmployeeRoleAssingment Resource");

		// Check-passed-Id
		MethodValidationUtils.checkIfIdIsZero(userRoleId, "userRoleId");

		// Fetching-All-Details-for-UserRoleId-Calling-to-service
		List<EmployeeRoleAssingment> matchingDetails = emplRoleAssignmentService.getMenuesForRoleID(userRoleId);

		// Convert-to-DTO
		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(matchingDetails);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
								responseDTOList.size() + ":Employee Role Assigments found Successfully")
						.results(responseDTOList).build());

	}// getEmployeeforRoleID

	@Override
	public ResponseEntity<Response> getEmployeeforCompanyId(int globalCompanyMasterId) {
		if (logger.isDebugEnabled())
			logger.debug("In getEmployeeforCompanyId Method from EmployeeRoleAssingment Resource");

		// Check-passed-Id
		MethodValidationUtils.checkIfIdIsZero(globalCompanyMasterId, "globalCompanyMasterId");

		// Fetching-All-Details-for-UserRoleId-Calling-to-service
		List<EmployeeRoleAssingment> matchingDetails = emplRoleAssignmentService
				.getMenuesForCompanyId(globalCompanyMasterId);

		// Convert-to-DTO
		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(matchingDetails);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
								responseDTOList.size() + ":Employee Role Assigments found Successfully")
						.results(responseDTOList).build());
	}// getEmployeeforCompanyId-Close

	@Override
	public ResponseEntity<Response> getEmployeeforCompanyGroupId(int comapnyGroupId) {
		if (logger.isDebugEnabled())
			logger.debug("In getEmployeeforCompanyGroupId Method from EmployeeRoleAssingment Resource");

		// Check-passed-Id
		MethodValidationUtils.checkIfIdIsZero(comapnyGroupId, "comapnyGroupId");

		// Fetching-All-Details-for-UserRoleId-Calling-to-service
		List<EmployeeRoleAssingment> matchingDetails = emplRoleAssignmentService
				.getEmployeeforCompanyGroupId(comapnyGroupId);

		// Convert-to-DTO
		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(matchingDetails);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
								responseDTOList.size() + ":Employee Role Assigments found Successfully")
						.results(responseDTOList).build());

	}// getEmployeeforUserGroupId-Close

	/*
	 * Add-New-EmployeeRoleAssginment-Details
	 */
	@Override
	public ResponseEntity<Response> addNewEmployeeRoleAssignment(List<EmployeeRoleAssingmentRequestDTO> requestDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In addNewEmployeeRoleAssignment Method from EmployeeRoleAssingment Resource");

		// Save-Entity-list
		List<EmployeeRoleAssingment> entityList = new ArrayList<EmployeeRoleAssingment>();

		// Check-Requested-list-is-empty?
		if (requestDTO.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("EmployeeRoleAssingmentRequestDTO list is empty.");

			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER,
					"Employee Role Assignment details are missing!");

		}

		// Perform-Basic-Validation
		for (EmployeeRoleAssingmentRequestDTO request : requestDTO) {

			if (request.getEmployeeRoleAssignmentId() > 0) {

				if (logger.isDebugEnabled())
					logger.debug("Auto-Generated field employeeRoleAssignmentId. Not Required.");

				throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER,
						"Auto-Generated field employeeRoleAssignmentId. Not Required.");
			}

			// Check-GlobalUserMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getGlobalUserMasterId(), "globalUserMasterId");

			// Check-companyGroupMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getCompanyGroupMasterId(), "companyGroupMasterId");

			// Check-globalCompanyMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getGlobalCompanyMasterId(), "globalCompanyMasterId");

			// Check-userRoleId
			MethodValidationUtils.checkIfIdIsZero(request.getUserRoleId(), "userRoleId");

			// Convert-to-DTO
			entityList.add(dtoMapper.dtoToEntity(request));

		} // for-Close

		if (logger.isDebugEnabled())
			logger.debug(entityList.size() + ":records are requested for creation");

		// Call-to-Service-Method
		List<EmployeeRoleAssingment> savedDetails = emplRoleAssignmentService.addNewEmployeeRoleAssignment(entityList);

		// Convert-to-DTO
		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(savedDetails);

		if (logger.isDebugEnabled())
			logger.debug(responseDTOList.size() + ":records are created successfully");

		if (savedDetails.size() == entityList.size()) {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(),
									responseDTOList.size() + ":Employee Role Assignments Added Successfully")
							.results(responseDTOList).build());

		} else {
			return ResponseEntity.status(HttpStatus.CREATED)
					.body(ResponseBuilder.builder()
							.status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(),
									responseDTOList.size() + ":Employee Role Assignments Added Successfully and "
											+ (entityList.size() - responseDTOList.size()) + " were already exist!")
							.results(responseDTOList).build());

		}

	}// addNewEmployeeRoleAssignment-Close

	/*
	 * Update-Existing-EmployeeRoleAssginment-Details
	 */
	@Override
	public ResponseEntity<Response> updateEmployeeRoleAssignment(List<EmployeeRoleAssingmentRequestDTO> requestDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In updateEmployeeRoleAssignment Method from EmployeeRoleAssingment Resource");

		// Save-Entity-list
		List<EmployeeRoleAssingment> entityList = new ArrayList<EmployeeRoleAssingment>();

		// Check-Requested-list-is-empty?
		if (requestDTO.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("EmployeeRoleAssingmentRequestDTO list is empty.");

			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER,
					"Employee Role Assignment details are missing!");

		}

		// Perform-Basic-Validation
		for (EmployeeRoleAssingmentRequestDTO request : requestDTO) {

			// Check-employeeRoleAssignmentId
			MethodValidationUtils.checkIfIdIsZero(request.getEmployeeRoleAssignmentId(), "employeeRoleAssignmentId");

			// Check-GlobalUserMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getGlobalUserMasterId(), "globalUserMasterId");

			// Check-companyGroupMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getCompanyGroupMasterId(), "companyGroupMasterId");

			// Check-globalCompanyMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getGlobalCompanyMasterId(), "globalCompanyMasterId");

			// Check-userRoleId
			MethodValidationUtils.checkIfIdIsZero(request.getUserRoleId(), "userRoleId");

			/*
			 * While-De-Activation-of-record-remark-field-is-mandatory
			 */
			if (request.getIsActive() == false) {
				if (!StringUtils.isValidString(request.getRemark())) {
					if (logger.isDebugEnabled())
						logger.debug("While De-Activation Remark is mandatory");

					throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER,
							"while De-Activation remark is mandatory. remark is invalid!");

				}
			}

			// Convert-to-DTO
			entityList.add(dtoMapper.dtoToEntity(request));

		} // for-Close

		if (logger.isDebugEnabled())
			logger.debug(entityList.size() + ":records are requested for updations");

		// Call-to-Service-Method
		List<EmployeeRoleAssingment> savedDetails = emplRoleAssignmentService.updateEmployeeRoleAssignment(entityList);

		// Convert-to-DTO
		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(savedDetails);

		if (logger.isDebugEnabled())
			logger.debug(responseDTOList.size() + " : are updated successfully");

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
								responseDTOList.size() + ":Employee Role Assignments updated successfully")
						.results(responseDTOList).build());

	}// updateEmployeeRoleAssignment-Close

	// Fetch-all-Assignment-details-by-userId
	@Override
	public ResponseEntity<Response> getAssignmentByUserId(int globalUserMasterId) {

		if (logger.isDebugEnabled())
			logger.debug("In getAssignmentByUserId Method from EmployeeRoleAssingment Resource");

		// Check-Id-is-valid
		MethodValidationUtils.checkIfIdIsZero(globalUserMasterId, "globalUserMasterId");

		// Call-to-service-method
		List<EmployeeRoleAssingment> existDetails = emplRoleAssignmentService
				.getAssignmentDetailsByUserId(globalUserMasterId);

		// Convert-to-DTO

		// Convert-to-DTO
		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(existDetails);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
								responseDTOList.size() + ":Employee Assigments found Successfully")
						.results(responseDTOList).build());

	}// getAssignmentByUserId-close

}
