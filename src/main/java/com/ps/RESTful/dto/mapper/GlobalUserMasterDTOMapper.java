package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.GlobalUserMasterRequestDTO;
import com.ps.RESTful.dto.response.GlobalUserMasterResponseDTO;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.entities.master.GroupDBMaster;
import com.ps.util.DateUtils;
import com.ps.util.StringUtils;

@Component
public class GlobalUserMasterDTOMapper
		implements AbstractDTOMapper<GlobalUserMasterRequestDTO, GlobalUserMasterResponseDTO, GlobalUserMaster> {

	Logger logger = Logger.getLogger(GlobalUserMasterDTOMapper.class);

	@Autowired
	GlobalCompanyMasterDTOMapper companyDTOMapper;
	// Request Mapping methods below

	@Override
	public GlobalUserMaster dtoToEntity(GlobalUserMasterRequestDTO userDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In UserDTOMApper mapping UserDTO to entity...");
		if (userDTO == null)
			return new GlobalUserMaster();

		GlobalUserMaster user = new GlobalUserMaster();

		user.setUserName(userDTO.getUserName());
		user.setEmployeeMasterId(userDTO.getEmployeeMasterId());
		user.setEmailId(userDTO.getEmailId());
		user.setMobileNumber(userDTO.getMobileNumber());
		user.setCreatedBy(userDTO.getCreatedBy());
		user.setLastModifiedBy(userDTO.getLastModifiedBy());
		user.setLoginStatus(userDTO.isLoginStatus());
		user.setLocked(userDTO.isLocked());
		user.setFailedLoginAttempt(userDTO.getFailedLoginAttempt());
		user.setPassword(userDTO.getPassword());
		user.setFirstName(userDTO.getFirstName());
		user.setLastName(userDTO.getLastName());

		/*
		 * Below-Mappings-was-not-there. Added-by-MayurG
		 */

		// Set-Company-details-this-was-missing-before-added-by-MayurG
		GlobalCompanyMaster companyMaster = new GlobalCompanyMaster();
		companyMaster.setGlobalCompanyMasterId(userDTO.getGlobalCompanyMasterId());
		user.setGlobalCompanyMaster(companyMaster);

		// Set-GroupDBMasterId-this-was-missing-before-added-by-MayurG
		GroupDBMaster groupDBMaster = new GroupDBMaster();
		groupDBMaster.setGroupCompanyMasterId(userDTO.getGroupDBMasterid());
		user.setGroupDBMaster(groupDBMaster);

		// Set-Dates
		user.setValidFrom(StringUtils.stringToDate(userDTO.getValidFrom()));
		user.setValidTo(StringUtils.stringToDate(userDTO.getValidTo()));

		if (userDTO.getEmployeeDetails() != null) {
			user.setUserName(
					userDTO.getEmployeeDetails().getFirstname() + " " + userDTO.getEmployeeDetails().getLastname());
		}

		// Date fields
		user.setActivateDate(StringUtils.stringToDate(userDTO.getActivateDate()));
		user.setDeActivateDate(StringUtils.stringToDate(userDTO.getDeActivateDate()));
		user.setLastLoginDateTime(StringUtils.stringToDate(userDTO.getLastLoginDateTime()));
		user.setLastPasswordChangeDateTime(StringUtils.stringToDate(userDTO.getLastPasswordChangeDateTime()));

		return user;
	}

	public List<GlobalUserMaster> dtoListToEntityList(List<GlobalUserMasterRequestDTO> userDTOList) {

		if (logger.isDebugEnabled())
			logger.debug("In UserDTOMApper dtoListToEntityList method " + "mapping UserDTO list to entity list");

		List<GlobalUserMaster> userMasterEntityList = new ArrayList<GlobalUserMaster>();

		if (!CollectionUtils.isEmpty(userDTOList)) {
			for (GlobalUserMasterRequestDTO userDTO : userDTOList) {
				GlobalUserMaster user = dtoToEntity(userDTO);
				if (user != null)
					userMasterEntityList.add(user);
			}
		}

		return userMasterEntityList;
	}

	// Response Mapping methods below

	@Override
	public GlobalUserMasterResponseDTO entityToDto(GlobalUserMaster user) {

		GlobalUserMasterResponseDTO userResponseDTO = new GlobalUserMasterResponseDTO();

		if (user != null) {

			userResponseDTO.setGlobalUserMasterId(user.getUserMasterId());
			userResponseDTO.setEmployeeMasterId(user.getEmployeeMasterId());
			userResponseDTO.setUserName(user.getUserName());
			userResponseDTO.setFirstName(user.getFirstName());
			userResponseDTO.setLastName(user.getLastName());
			userResponseDTO.setEmailId(user.getEmailId());
			userResponseDTO.setMobileNumber(user.getMobileNumber());
			userResponseDTO.setLocked(user.isLocked());
			userResponseDTO.setActive(user.isActive());
			
			userResponseDTO.setCreatedBy(user.getCreatedBy());
			userResponseDTO.setCreateDate(StringUtils.dateToString(user.getCreateDate()));
			userResponseDTO.setLastModifiedBy(user.getLastModifiedBy());
			userResponseDTO.setLastModifiedDateTime(StringUtils.dateToString(user.getLastModifiedDateTime()));

			// Added-Company-Id-by-MayurG:-This-information-was-missing
			userResponseDTO.setGlobalCompanyMasterId(user.getGlobalCompanyMaster().getGlobalCompanyMasterId());
			userResponseDTO.setGroupDBMasterid(user.getGroupDBMaster().getGroupCompanyMasterId());

			if (user.getPasswordDueDateTime() != null)
				userResponseDTO.setPasswordDueDateTime(DateUtils.getDateString(user.getPasswordDueDateTime()));

			if (user.getNextPasswordChangeDateTime() != null)
				userResponseDTO
						.setNextPasswordChangeDateTime(DateUtils.getDateString(user.getNextPasswordChangeDateTime()));

			if (user.getGlobalCompanyMaster() != null)
				userResponseDTO.setCompany(companyDTOMapper.entityToDto(user.getGlobalCompanyMaster()));
		}

		return userResponseDTO;
	}

}