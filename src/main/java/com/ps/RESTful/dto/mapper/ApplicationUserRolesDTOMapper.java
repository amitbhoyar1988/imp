package com.ps.RESTful.dto.mapper;

import org.springframework.stereotype.Component;

import com.ps.RESTful.dto.request.ApplicationUserRolesRequestDTO;
import com.ps.RESTful.dto.response.ApplicationUserRolesResponseDTO;
import com.ps.entities.master.ApplicationUserRoles;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.util.StringUtils;

@Component
public class ApplicationUserRolesDTOMapper implements
		AbstractDTOMapper<ApplicationUserRolesRequestDTO, ApplicationUserRolesResponseDTO, ApplicationUserRoles> {

	@Override
	public ApplicationUserRoles dtoToEntity(ApplicationUserRolesRequestDTO requestDTO) {

		ApplicationUserRoles roles = new ApplicationUserRoles();

		roles.setApplicationRoleId(requestDTO.getApplicationRoleId());

		CompanyGroupMaster companyGroup = new CompanyGroupMaster();
		companyGroup.setCompanyGroupMasterId(requestDTO.getCompanyGroupMasterId());
		roles.setCompanyGroupMaster(companyGroup);

		GlobalCompanyMaster companyMaster = new GlobalCompanyMaster();
		companyMaster.setGlobalCompanyMasterId(requestDTO.getGlobalCompanyMasterId());
		roles.setGlobalCompanyMaster(companyMaster);

		roles.setRoleName(requestDTO.getRoleName());
		roles.setRoleDiscription(requestDTO.getRoleDiscription());
		roles.setIsActive(requestDTO.getIsActive());

		roles.setCreatedBy(requestDTO.getCreatedBy());

		roles.setCreatedDateTime(StringUtils.stringToDate(requestDTO.getCreatedDateTime()));

		roles.setLastModifiedBy(requestDTO.getLastModifiedBy());
		roles.setLastModifiedDateTime(StringUtils.stringToDate(requestDTO.getLastModifiedDateTime()));

		return roles;
	}

	@Override
	public ApplicationUserRolesResponseDTO entityToDto(ApplicationUserRoles entity) {

		ApplicationUserRolesResponseDTO resonseDTO = new ApplicationUserRolesResponseDTO();

		resonseDTO.setApplicationRoleId(entity.getApplicationRoleId());

		resonseDTO.setCompanyGroupMasterId(entity.getCompanyGroupMaster().getCompanyGroupMasterId());

		resonseDTO.setGlobalCompanyMasterId(entity.getGlobalCompanyMaster().getGlobalCompanyMasterId());

		resonseDTO.setRoleName(entity.getRoleName());
		resonseDTO.setRoleDiscription(entity.getRoleDiscription());
		resonseDTO.setIsActive(entity.getIsActive());

		resonseDTO.setCreatedBy(entity.getCreatedBy());

		resonseDTO.setCreatedDateTime(StringUtils.dateToString(entity.getCreatedDateTime()));

		resonseDTO.setLastModifiedBy(entity.getLastModifiedBy());
		resonseDTO.setLastModifiedDateTime(StringUtils.dateToString(entity.getLastModifiedDateTime()));

		return resonseDTO;
	}

}
