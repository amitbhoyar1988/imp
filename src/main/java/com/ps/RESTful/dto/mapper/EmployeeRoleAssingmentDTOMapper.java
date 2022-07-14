package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.EmployeeRoleAssingmentRequestDTO;
import com.ps.RESTful.dto.response.EmployeeRoleAssingmentResponseDTO;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.EmployeeRoleAssingment;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.entities.master.UserRole;
import com.ps.util.StringUtils;

@Component
public class EmployeeRoleAssingmentDTOMapper implements
		AbstractDTOMapper<EmployeeRoleAssingmentRequestDTO, EmployeeRoleAssingmentResponseDTO, EmployeeRoleAssingment> {

	@Override
	public EmployeeRoleAssingment dtoToEntity(EmployeeRoleAssingmentRequestDTO requestDTO) {

		EmployeeRoleAssingment details = new EmployeeRoleAssingment();

		details.setEmployeeRoleAssignmentId(requestDTO.getEmployeeRoleAssignmentId());

		// Set-User-Details
		GlobalUserMaster userMaster = new GlobalUserMaster();
		userMaster.setUserMasterId(requestDTO.getGlobalUserMasterId());
		details.setGlobalUserMaster(userMaster);

		// Set-CompanyGroup
		CompanyGroupMaster groupCompany = new CompanyGroupMaster();
		groupCompany.setCompanyGroupMasterId(requestDTO.getCompanyGroupMasterId());
		details.setCompanyGroupMaster(groupCompany);

		// Set-company
		GlobalCompanyMaster company = new GlobalCompanyMaster();
		company.setGlobalCompanyMasterId(requestDTO.getGlobalCompanyMasterId());
		details.setGlobalCompanyMaster(company);

		// Set-Role
		UserRole roleDetails = new UserRole();
		roleDetails.setUserRoleId(requestDTO.getUserRoleId());
		details.setUserRoles(roleDetails);

		details.setCreatedBy(requestDTO.getCreatedBy());
		details.setCreatedDateTime(StringUtils.stringToDate(requestDTO.getCreatedDateTime()));

		details.setLastModifiedBy(requestDTO.getLastModifiedBy());
		details.setLastModifiedDateTime(StringUtils.stringToDate(requestDTO.getLastModifiedDateTime()));

		details.setRemark(requestDTO.getRemark());
		details.setIsActive(requestDTO.getIsActive());

		return details;
	}

	@Override
	public EmployeeRoleAssingmentResponseDTO entityToDto(EmployeeRoleAssingment entity) {

		EmployeeRoleAssingmentResponseDTO respondeDTO = new EmployeeRoleAssingmentResponseDTO();

		respondeDTO.setEmployeeRoleAssignmentId(entity.getEmployeeRoleAssignmentId());

		// Set-User-Details
		respondeDTO.setGlobalUserMasterId(entity.getGlobalUserMaster().getUserMasterId());
		respondeDTO.setUserName(entity.getGlobalUserMaster().getUserName());
		respondeDTO.setEmployeeMasterId(entity.getGlobalUserMaster().getEmployeeMasterId());
		respondeDTO.setEmailId(entity.getGlobalUserMaster().getEmailId());
		respondeDTO.setMobileNumber(entity.getGlobalUserMaster().getMobileNumber());
		respondeDTO.setUserIsActive(entity.getGlobalUserMaster().isActive());
		respondeDTO.setLocked(entity.getGlobalUserMaster().isLocked());
		
		// Set-CompanyGroup
		respondeDTO.setCompanyGroupMasterId(entity.getCompanyGroupMaster().getCompanyGroupMasterId());
		respondeDTO.setCompanyGroupName(entity.getCompanyGroupMaster().getCompanyGroupName());

		// Set-company
		respondeDTO.setGlobalCompanyMasterId(entity.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		respondeDTO.setCompanyName(entity.getGlobalCompanyMaster().getCompanyName());
		
		// Set-Role-Details
		respondeDTO.setUserRoleId(entity.getUserRoles().getUserRoleId());
		respondeDTO.setRoleName(entity.getUserRoles().getRoleName());

		// Set-UserGroup-Details-of-userRole
		respondeDTO.setUserGroupId(entity.getUserRoles().getUserGroup().getUserGroupId());
		respondeDTO.setGroupName(entity.getUserRoles().getUserGroup().getGroupName());

		respondeDTO.setCreatedBy(entity.getCreatedBy());
		respondeDTO.setCreatedDateTime(StringUtils.dateToString(entity.getCreatedDateTime()));
		respondeDTO.setLastModifiedBy(entity.getLastModifiedBy());
		respondeDTO.setLastModifiedDateTime(StringUtils.dateToString(entity.getLastModifiedDateTime()));

		respondeDTO.setRemark(entity.getRemark());
		respondeDTO.setIsActive(entity.getIsActive());

		return respondeDTO;
	}

	public List<EmployeeRoleAssingmentResponseDTO> entityListToDtoList(List<EmployeeRoleAssingment> entity) {

		if (CollectionUtils.isEmpty(entity))
			return new ArrayList<>();

		List<EmployeeRoleAssingmentResponseDTO> responseDTOList = new ArrayList<>();

		for (EmployeeRoleAssingment listValue : entity) {
			EmployeeRoleAssingmentResponseDTO responseDTO = entityToDto(listValue);
			if (responseDTO != null)
				responseDTOList.add(responseDTO);
		}
		return responseDTOList;

	}

	public List<EmployeeRoleAssingment> dtoListToEntityList(List<EmployeeRoleAssingmentRequestDTO> requestDTOList) {

		if (CollectionUtils.isEmpty(requestDTOList))
			return new ArrayList<>();

		List<EmployeeRoleAssingment> list = new ArrayList<>();

		for (EmployeeRoleAssingmentRequestDTO listValue : requestDTOList) {
			EmployeeRoleAssingment entityData = dtoToEntity(listValue);
			if (entityData != null)
				list.add(entityData);
		}
		return list;
	}

}
