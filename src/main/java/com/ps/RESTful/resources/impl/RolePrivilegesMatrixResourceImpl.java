package com.ps.RESTful.resources.impl;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.RolePrivilegesMatrixDTOMapper;
import com.ps.RESTful.dto.request.RolePrivilegesMatrixRequestDTO;
import com.ps.RESTful.dto.response.ApplicationUserRolewiseMenuesResponseDTO;
import com.ps.RESTful.dto.response.RolePrivilegesMatrixResponseDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.resources.RolePrivilegesMatrixResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.beans.ApplicationRoleWiseMenuDetailsBean;
import com.ps.entities.master.RolePrivilegesMatrix;
import com.ps.services.RolePrivilegesMatrixService;
import com.ps.util.MethodValidationUtils;

@RestController
@RequestMapping(path = RolePrivilegesMatrixResource.RESOURCE_PATH)
public class RolePrivilegesMatrixResourceImpl implements RolePrivilegesMatrixResource {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	RolePrivilegesMatrixDTOMapper dtoMapper;

	@Autowired
	RolePrivilegesMatrixService rolePrivilegesMatrixService;

	@Override
	public ResponseEntity<Response> getMenuesforRoleID(int userRoleId) {

		if (logger.isDebugEnabled())
			logger.debug("In getMenuesforRoleID method from RolePrivilegesMatrix Resource");

		// check-Id-is-passed?
		if (userRoleId == 0) {
			if (logger.isDebugEnabled())
				logger.debug("userRoleId is invalid!");

			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "userRoleId is invalid!");
		}

		// Call-to-Service-Method
		List<RolePrivilegesMatrix> matrixDetails = rolePrivilegesMatrixService.getMenuesForRoleID(userRoleId);

		/*
		 * List<RolePrivilegesMatrixResponseDTO> responseDTOList = new ArrayList<>();
		 * 
		 * // Convert-Entity-to-DTO for (RolePrivilegesMatrix iterate : matrixDetails) {
		 * RolePrivilegesMatrixResponseDTO rolePrivilegesMatrixResponseDTO =
		 * dtoMapper.entityToDto(iterate);
		 * 
		 * responseDTOList.add(rolePrivilegesMatrixResponseDTO); }
		 */

		// Convert-Bean-to-DTO
		List<ApplicationUserRolewiseMenuesResponseDTO> responseDTOList = new ArrayList<>();

		for (RolePrivilegesMatrix beanIterate : matrixDetails) {
			ApplicationRoleWiseMenuDetailsBean bean = new ApplicationRoleWiseMenuDetailsBean(beanIterate,
					beanIterate.getUserRoles(), beanIterate.getApplicationMenus());
			ApplicationUserRolewiseMenuesResponseDTO response = bean.beanToDTO(bean);
			responseDTOList.add(response);

		} // for-Close

		/*
		 * Sorting-Data
		 * 
		 * Collections.sort(responseDTOList, new
		 * Comparator<ApplicationUserRolewiseMenuesResponseDTO>() {
		 * 
		 * @Override public int compare(ApplicationUserRolewiseMenuesResponseDTO o1,
		 * ApplicationUserRolewiseMenuesResponseDTO o2) {
		 * 
		 * return new CompareToBuilder()
		 * .append(o1.getAccessibleMenuDetail().getMainMenuName().toUpperCase(),
		 * o2.getAccessibleMenuDetail().getMainMenuName().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName2() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName2().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName2() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName2().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName3() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName3().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName3() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName3().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName4() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName4().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName4() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName4().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName5() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName5().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName5() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName5().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName6() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName6().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName6() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName6().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName7() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName7().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName7() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName7().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName8() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName8().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName8() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName8().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName9() == null) ? "NA"
		 * : o1.getAccessibleMenuDetail().getSubMenuOrFormName9().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName9() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName9().toUpperCase())
		 * .append((o1.getAccessibleMenuDetail().getSubMenuOrFormName10() == null) ?
		 * "NA" : o1.getAccessibleMenuDetail().getSubMenuOrFormName10().toUpperCase(),
		 * (o2.getAccessibleMenuDetail().getSubMenuOrFormName10() == null) ? "NA" :
		 * o2.getAccessibleMenuDetail().getSubMenuOrFormName10().toUpperCase())
		 * .toComparison(); } });
		 *
		 */
		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder().status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
						"Role wise Menu details found Successfully").results(responseDTOList).build());
	}// getMenuesforRoleID-Close

	/*
	 * ADD-New-Privileges
	 */
	@Override
	public ResponseEntity<Response> addPrivileges(List<RolePrivilegesMatrixRequestDTO> requestDTOList) {

		if (logger.isDebugEnabled())
			logger.debug("In addPrivileges method from RolePrivilegesMatrix Resource");

		// Check-Requested-list-is-empty?
		if (requestDTOList.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("RolePrivilegesMatrixRequestDTO list is empty.");

			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Privileges details are missing!");
		}

		// Basic-Validation
		// Check-UserRoleId-and-globalCompanyMasterId-are-passed?
		for (RolePrivilegesMatrixRequestDTO request : requestDTOList) {

			if (request.getRolePrivilegeMatrixId() > 0) {

				if (logger.isDebugEnabled())
					logger.debug("Auto-Generated field rolePrivilegeMatrixId. Not Required.");

				throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER,
						"Auto-Generated field rolePrivilegeMatrixId. Not Required.");
			}

			// Check-userRoleId
			MethodValidationUtils.checkIfIdIsZero(request.getUserRoleId(), "userRoleId");

			// Check-globalCompanyMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getGlobalCompanyMasterId(), "globalCompanyMasterId");

		} // Basic-Validation-close

		// Convert-to-Entity
		List<RolePrivilegesMatrix> rolePrivilegesMatrixDetails = dtoMapper.dtoListToEntityList(requestDTOList);

		// Call-to-Service-Method
		List<RolePrivilegesMatrix> savedDetails = rolePrivilegesMatrixService
				.addPrivileges(rolePrivilegesMatrixDetails);

		// Convert-entity-to-DTO
		List<RolePrivilegesMatrixResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(savedDetails);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.CREATED.getCode(),
								responseDTOList.size() + ":Role Privileges Added Successfully")
						.results(responseDTOList).build());
	}// Add-Privileges-Close

	/*
	 * Update-Exist-Privileges
	 */
	@Override
	public ResponseEntity<Response> updatePrivileges(List<RolePrivilegesMatrixRequestDTO> requestDTOList) {
		if (logger.isDebugEnabled())
			logger.debug("In updatePrivileges method from RolePrivilegesMatrix Resource");

		// Check-Requested-list-is-empty?
		if (requestDTOList.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("RolePrivilegesMatrixRequestDTO list is empty.");

			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Privileges details are missing!");
		}

		// Basic-Validation
		// Check-UserRoleId-and-globalCompanyMasterId-are-passed?
		for (RolePrivilegesMatrixRequestDTO request : requestDTOList) {

			// Check-rolePrivilegeMatrixId
			MethodValidationUtils.checkIfIdIsZero(request.getRolePrivilegeMatrixId(), "rolePrivilegeMatrixId");

			// Check-userRoleId
			MethodValidationUtils.checkIfIdIsZero(request.getUserRoleId(), "userRoleId");

			// Check-globalCompanyMasterId
			MethodValidationUtils.checkIfIdIsZero(request.getGlobalCompanyMasterId(), "globalCompanyMasterId");

		} // Basic-Validation-close

		// Convert-to-Entity
		List<RolePrivilegesMatrix> rolePrivilegesMatrixDetails = dtoMapper.dtoListToEntityList(requestDTOList);

		// Call-to-Service-Method
		List<RolePrivilegesMatrix> savedDetails = rolePrivilegesMatrixService
				.updatePrivileges(rolePrivilegesMatrixDetails);

		// Convert-entity-to-DTO
		List<RolePrivilegesMatrixResponseDTO> responseDTOList = dtoMapper.entityListToDtoList(savedDetails);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
								responseDTOList.size() + ":Role Privileges updated Successfully")
						.results(responseDTOList).build());
	}

}
