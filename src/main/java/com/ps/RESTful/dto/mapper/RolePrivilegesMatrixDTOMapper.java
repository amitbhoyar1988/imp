package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.RolePrivilegesMatrixRequestDTO;
import com.ps.RESTful.dto.response.RolePrivilegesMatrixResponseDTO;
import com.ps.entities.master.ApplicationMenus;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.RolePrivilegesMatrix;
import com.ps.entities.master.UserRole;
import com.ps.util.StringUtils;

@Component
public class RolePrivilegesMatrixDTOMapper implements
		AbstractDTOMapper<RolePrivilegesMatrixRequestDTO, RolePrivilegesMatrixResponseDTO, RolePrivilegesMatrix> {

	@Autowired
	ApplicationMenusDTOMapper menuDTOMapper;

	@Override
	public RolePrivilegesMatrix dtoToEntity(RolePrivilegesMatrixRequestDTO requestDTO) {

		RolePrivilegesMatrix roleMatrix = new RolePrivilegesMatrix();

		roleMatrix.setRolePrivilegeMatrixId(requestDTO.getRolePrivilegeMatrixId());

		// Set-UserRole
		UserRole userRole = new UserRole();
		userRole.setUserRoleId(requestDTO.getUserRoleId());
		roleMatrix.setUserRoles(userRole);

		// Set-Menu
		ApplicationMenus menusDetails = new ApplicationMenus();
		menusDetails.setApplicationMenuId(requestDTO.getApplicationMenusId());
		roleMatrix.setApplicationMenus(menusDetails);

		// Set-Company
		GlobalCompanyMaster company = new GlobalCompanyMaster();
		company.setGlobalCompanyMasterId(requestDTO.getGlobalCompanyMasterId());
		roleMatrix.setGlobalCompanyMaster(company);

		roleMatrix.setReadAccess(requestDTO.getReadAccess());
		roleMatrix.setWriteAccess(requestDTO.getWriteAccess());
		roleMatrix.setModifyAccess(requestDTO.getModifyAccess());
		roleMatrix.setDeleteAccess(requestDTO.getDeleteAccess());

		roleMatrix.setCreatedBy(requestDTO.getCreatedBy());
		roleMatrix.setCreatedDateTime(StringUtils.stringToDate(requestDTO.getCreatedDateTime()));

		roleMatrix.setLastModifiedBy(requestDTO.getLastModifiedBy());
		roleMatrix.setLastModifiedDateTime(StringUtils.stringToDate(requestDTO.getLastModifiedDateTime()));

		roleMatrix.setIsActive(requestDTO.getIsActive());

		return roleMatrix;
	}

	@Override
	public RolePrivilegesMatrixResponseDTO entityToDto(RolePrivilegesMatrix entity) {

		RolePrivilegesMatrixResponseDTO responseDTO = new RolePrivilegesMatrixResponseDTO();

		responseDTO.setRolePrivilegeMatrixId(entity.getRolePrivilegeMatrixId());

		responseDTO.setUserRoleId(entity.getUserRoles().getUserRoleId());
		responseDTO.setApplicationMenusId(entity.getApplicationMenus().getApplicationMenuId());
		responseDTO.setGlobalCompanyMasterId(entity.getGlobalCompanyMaster().getGlobalCompanyMasterId());

		responseDTO.setReadAccess(entity.getReadAccess());
		responseDTO.setWriteAccess(entity.getWriteAccess());
		responseDTO.setModifyAccess(entity.getModifyAccess());
		responseDTO.setDeleteAccess(entity.getDeleteAccess());

		responseDTO.setCreatedBy(entity.getCreatedBy());
		responseDTO.setCreatedDateTime(StringUtils.dateToString(entity.getCreatedDateTime()));

		responseDTO.setLastModifiedBy(entity.getLastModifiedBy());
		responseDTO.setLastModifiedDateTime(StringUtils.dateToString(entity.getLastModifiedDateTime()));

		responseDTO.setIsActive(entity.getIsActive());

		return responseDTO;
	}

	public List<RolePrivilegesMatrixResponseDTO> entityListToDtoList(List<RolePrivilegesMatrix> entity) {

		if (CollectionUtils.isEmpty(entity))
			return new ArrayList<>();

		List<RolePrivilegesMatrixResponseDTO> responseDTOList = new ArrayList<>();

		for (RolePrivilegesMatrix listValue : entity) {
			RolePrivilegesMatrixResponseDTO responseDTO = entityToDto(listValue);
			if (responseDTO != null)
				responseDTOList.add(responseDTO);
		}
		return responseDTOList;

	}

	public List<RolePrivilegesMatrix> dtoListToEntityList(List<RolePrivilegesMatrixRequestDTO> requestDTOList) {

		if (CollectionUtils.isEmpty(requestDTOList))
			return new ArrayList<>();

		List<RolePrivilegesMatrix> list = new ArrayList<>();

		for (RolePrivilegesMatrixRequestDTO listValue : requestDTOList) {
			RolePrivilegesMatrix entityData = dtoToEntity(listValue);
			if (entityData != null)
				list.add(entityData);
		}
		return list;
	}

}
