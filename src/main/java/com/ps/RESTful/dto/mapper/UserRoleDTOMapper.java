package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;

import com.ps.RESTful.dto.request.UserRoleRequestDTO;
import com.ps.RESTful.dto.response.UserRoleResponseDTO;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupHistory;
import com.ps.entities.master.UserRole;
import com.ps.entities.master.UserRoleHistory;
import com.ps.util.StringUtils;

@Component
public class UserRoleDTOMapper implements AbstractDTOMapper<UserRoleRequestDTO, UserRoleResponseDTO, UserRole>{

	
	@Override
	public UserRole dtoToEntity(UserRoleRequestDTO dto) {

		if (dto == null)
			return new UserRole();
		UserRole master = new UserRole();
		
		master.setUserRoleId(dto.getUserRoleId());		
		master.setRoleName(dto.getRoleName());
		master.setRoleDescription(dto.getRoleDescription());
		
		UserGroup userGroup = new UserGroup();		
		userGroup.setUserGroupId(dto.getUserGroupId());
		master.setUserGroup(userGroup); 		
		
		master.setRemark(dto.getRemark());
		master.setDefault(dto.isDefault());
		master.setActive(dto.isActive());
		master.setCreatedBy(dto.getCreatedBy());
		master.setCreatedDateTime(StringUtils.stringToDate(dto.getCreatedDateTime()));
		master.setLastModifiedBy(dto.getLastModifiedBy());
		master.setLastModifiedDateTime(StringUtils.stringToDate(dto.getLastModifiedDateTime()));
		return master;
	}
	
	
	@Override
	public UserRoleResponseDTO entityToDto(UserRole entity) {

		UserRoleResponseDTO dto = new UserRoleResponseDTO();
		
		dto.setUserRoleId(entity.getUserRoleId());		
		dto.setRoleName(entity.getRoleName());
		dto.setRoleDescription(entity.getRoleDescription());			
		dto.setUserGroupId(entity.getUserGroup().getUserGroupId());	
		dto.setGroupName(entity.getUserGroup().getGroupName());	
		dto.setRemark(entity.getRemark());	
		dto.setDefault(entity.isDefault());
		dto.setActive(entity.isActive());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedDateTime(StringUtils.dateToString(entity.getCreatedDateTime()));
		dto.setLastModifiedBy(entity.getLastModifiedBy());
		dto.setLastModifiedDateTime(StringUtils.dateToString(entity.getLastModifiedDateTime()));		
		return dto;
	}
	
	public List<UserRoleResponseDTO> entityListToDtoList(List<UserRole> entityList) {
		List<UserRoleResponseDTO> responseDTOList = new ArrayList<UserRoleResponseDTO>();
		entityList.forEach(s -> {
			responseDTOList.add(entityToDto(s));			
		});
		return responseDTOList;
	}
	
	public UserRoleHistory entityToEntityHistory(UserRole dBDetails,
			UserRoleHistory history) {
		history.setUserRoleId(dBDetails.getUserRoleId());		
		history.setRoleName(dBDetails.getRoleName());
		history.setRoleDescription(dBDetails.getRoleDescription());		
		history.setUserGroupId(dBDetails.getUserGroup().getUserGroupId());			
		history.setRemark(dBDetails.getRemark());	
		history.setDefault(dBDetails.isDefault());
		history.setActive(dBDetails.isActive());
		history.setCreatedBy(dBDetails.getCreatedBy());
		history.setCreatedDateTime(dBDetails.getCreatedDateTime());
		history.setLastModifiedBy(dBDetails.getLastModifiedBy());
		history.setLastModifiedDateTime(dBDetails.getLastModifiedDateTime());
		return history;
	}
	
	public Optional<UserRole> entityToEntity(UserRole req,
			Optional<UserRole> dBDetails) {
		if (!dBDetails.isPresent())
			return Optional.empty();

		dBDetails.get().setUserRoleId(req.getUserRoleId());		
		dBDetails.get().setRoleName(req.getRoleName());
		dBDetails.get().setRoleDescription(req.getRoleDescription());	
		
		UserGroup groupMaster = new UserGroup();		
		groupMaster.setUserGroupId(req.getUserGroup().getUserGroupId());		
		dBDetails.get().setUserGroup(groupMaster);		
		
		dBDetails.get().setRemark(req.getRemark());	
		dBDetails.get().setDefault(req.isDefault());
		dBDetails.get().setActive(req.isActive());
		dBDetails.get().setLastModifiedBy(req.getLastModifiedBy());
		dBDetails.get().setLastModifiedDateTime(req.getLastModifiedDateTime());					
		return dBDetails;
	}
	
}
