/**
 * 
 */
package com.ps.beans;

import com.ps.RESTful.dto.mapper.ApplicationMenusDTOMapper;
import com.ps.RESTful.dto.mapper.UserRoleDTOMapper;
import com.ps.RESTful.dto.response.ApplicationUserRolewiseMenuesResponseDTO;
import com.ps.entities.master.ApplicationMenus;
import com.ps.entities.master.RolePrivilegesMatrix;
import com.ps.entities.master.UserRole;
import com.ps.util.StringUtils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MayurG: This Bean will be used to hold the details of Application
 *         User Details for His Menus which he can access. Information
 *         Includes:- 1. Role Detail 2. Menu Details and its privileges.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
public class ApplicationRoleWiseMenuDetailsBean {

	private RolePrivilegesMatrix rolePrivilegesMatrixDetails;
	private UserRole userRoleDetail;
	private ApplicationMenus accessibleMenuDetails;

	public ApplicationUserRolewiseMenuesResponseDTO beanToDTO(
			ApplicationRoleWiseMenuDetailsBean applicationUserDetailBean) {
		ApplicationUserRolewiseMenuesResponseDTO responseDTO = new ApplicationUserRolewiseMenuesResponseDTO();

		responseDTO.setRolePrivilegeMatrixId(
				applicationUserDetailBean.getRolePrivilegesMatrixDetails().getRolePrivilegeMatrixId());
		responseDTO.setGlobalCompanyMasterId(applicationUserDetailBean.getRolePrivilegesMatrixDetails()
				.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		responseDTO.setCompanyName(
				applicationUserDetailBean.getRolePrivilegesMatrixDetails().getGlobalCompanyMaster().getCompanyName());

		// Set-Application-User-Role-Details
		UserRoleDTOMapper roleDTOMapper = new UserRoleDTOMapper();
		responseDTO.setUserRoleDetail(roleDTOMapper.entityToDto(applicationUserDetailBean.getUserRoleDetail()));

		// Set-Application-Menu-Details
		ApplicationMenusDTOMapper menuDTOmapper = new ApplicationMenusDTOMapper();
		responseDTO.setAccessibleMenuDetail(
				menuDTOmapper.entityToDto(applicationUserDetailBean.getAccessibleMenuDetails()));

		// responseDTO.setMenuDetails(applicationUserDetailBean.getApplicationMenuDetails());

		// Set-Access-details
		responseDTO.setReadAccess(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getReadAccess());
		responseDTO.setWriteAccess(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getWriteAccess());
		responseDTO.setModifyAccess(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getModifyAccess());
		responseDTO.setDeleteAccess(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getDeleteAccess());

		responseDTO.setIsActive(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getIsActive());
		responseDTO.setCreatedBy(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getCreatedBy());
		responseDTO.setCreatedDateTime(StringUtils
				.dateToString(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getCreatedDateTime()));
		responseDTO.setLastModifiedBy(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getLastModifiedBy());
		responseDTO.setLastModifiedDateTime(StringUtils
				.dateToString(applicationUserDetailBean.getRolePrivilegesMatrixDetails().getLastModifiedDateTime()));

		return responseDTO;
	}
}
