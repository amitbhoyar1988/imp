package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.UserGroupRequestDTO;
import com.ps.RESTful.dto.response.UserGroupResponseDTO;
import com.ps.beans.UserGroupBean;
import com.ps.beans.UserGroupRequestBean;
import com.ps.dto.UserGroupDTO;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.CompanyGroupMasterBean;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupHistory;
import com.ps.entities.master.UserGroupReport;
import com.ps.util.StringUtils;


@Component
public class UserGroupDTOMapper implements AbstractDTOMapper<UserGroupRequestDTO, UserGroupResponseDTO, UserGroup>{

	
	@Override
	public UserGroup dtoToEntity(UserGroupRequestDTO dto) {

		if (dto == null)
			return new UserGroup();
		UserGroup master = new UserGroup();
		
		master.setUserGroupId(dto.getUserGroupId());		
		master.setGroupName(dto.getGroupName());
		master.setGroupDescription(dto.getGroupDescription());	
		
		CompanyGroupMaster groupMaster = new CompanyGroupMaster();		
		groupMaster.setCompanyGroupMasterId(dto.getCompanyGroupMasterId());
		master.setCompanyGroupMaster(groupMaster);
		
		master.setRemark(dto.getRemark());	
		master.setActive(dto.isActive());
		master.setCreatedBy(dto.getCreatedBy());
		master.setCreatedDateTime(StringUtils.stringToDate(dto.getCreatedDateTime()));
		master.setLastModifiedBy(dto.getLastModifiedBy());
		master.setLastModifiedDateTime(StringUtils.stringToDate(dto.getLastModifiedDateTime()));
		return master;
	}
	
	
	public List<UserGroup> dtoToEntity11(UserGroupRequestDTO dto) {

		List<UserGroup> userGroupList = new ArrayList<>();
		
		if (dto == null)
			return  userGroupList;	
		
		
		List<UserGroupBean> userGroupBeanList = new ArrayList<>();
		userGroupBeanList =  dto.getCompanyGroupMasterIds();
		for (int i = 0; i < userGroupBeanList.size(); i++) {
			UserGroup master = new UserGroup();
			UserGroupBean usrGrpBean = new UserGroupBean();
			
			usrGrpBean = userGroupBeanList.get(i);
			
			master.setGroupName(dto.getGroupName());
			master.setGroupDescription(dto.getGroupDescription());	
			
			master.setUserGroupId(usrGrpBean.getUserGroupId());	
			if(!dto.isActive()) {
				master.setActive(dto.isActive());
			}else {
				master.setActive(usrGrpBean.isActive());
			}						
			
			CompanyGroupMaster groupMaster = new CompanyGroupMaster();		
			groupMaster.setCompanyGroupMasterId(usrGrpBean.getCompanyGroupMasterId());
			master.setCompanyGroupMaster(groupMaster);
			
			master.setDefault(dto.isDefault());
			master.setRemark(dto.getRemark());	
//			master.setActive(dto.isActive());
//			master.setCreatedBy(dto.getCreatedBy());
//			master.setCreatedDateTime(StringUtils.stringToDate(dto.getCreatedDateTime()));
//			master.setLastModifiedBy(dto.getLastModifiedBy());
//			master.setLastModifiedDateTime(StringUtils.stringToDate(dto.getLastModifiedDateTime()));
			userGroupList.add(master);
		}
		
		
		return userGroupList;
	}
	
	
	@Override
	public UserGroupResponseDTO entityToDto(UserGroup entity) {

		UserGroupResponseDTO dto = new UserGroupResponseDTO();
		
		dto.setUserGroupId(entity.getUserGroupId());		
		dto.setGroupName(entity.getGroupName());
		dto.setGroupDescription(entity.getGroupDescription());	
		dto.setCompanyGroupMasterId(entity.getCompanyGroupMaster().getCompanyGroupMasterId());
	
		CompanyGroupMasterBean master = new CompanyGroupMasterBean();
		master.setCompanyGroupMasterId(entity.getCompanyGroupMaster().getCompanyGroupMasterId());
		master.setCompanyGroupCode(entity.getCompanyGroupMaster().getCompanyGroupCode());
		master.setCompanyGroupName(entity.getCompanyGroupMaster().getCompanyGroupName());
		master.setShortName(entity.getCompanyGroupMaster().getShortName());
		master.setActive(entity.getCompanyGroupMaster().isActive());
		master.setUserGroupId(entity.getUserGroupId());
		dto.setCompanyGroupMasterBean(master);
		
		dto.setRemark(entity.getRemark());	
		dto.setActive(entity.isActive());
		dto.setCreatedBy(entity.getCreatedBy());
		dto.setCreatedDateTime(StringUtils.dateToString(entity.getCreatedDateTime()));
		dto.setLastModifiedBy(entity.getLastModifiedBy());
		dto.setLastModifiedDateTime(StringUtils.dateToString(entity.getLastModifiedDateTime()));		
		return dto;
	}
	

	public UserGroupResponseDTO entityToDtoNew(UserGroupReport entity) {
		UserGroupResponseDTO dto = new UserGroupResponseDTO();				
		dto.setGroupName(entity.getGroupName());
		dto.setGroupDescription(entity.getGroupDescription());			
		dto.setRemark(entity.getRemark());	
		dto.setDefault(entity.isDefault());
		dto.setActive(entity.isActive());			
		return dto;
	}
	
	public List<UserGroupResponseDTO> entityListToDtoListNew(List<UserGroupReport> entityList) {
		List<UserGroupResponseDTO> responseDTOList = new ArrayList<UserGroupResponseDTO>();
		entityList.forEach(s -> {
			responseDTOList.add(entityToDtoNew(s));			
		});
		return responseDTOList;
	}
	
	public List<UserGroupResponseDTO> entityListToDtoList(List<UserGroup> entityList) {
		List<UserGroupResponseDTO> responseDTOList = new ArrayList<UserGroupResponseDTO>();
		entityList.forEach(s -> {
			responseDTOList.add(entityToDto(s));			
		});
		return responseDTOList;
	}
	
	public List<UserGroup> dtoListToEntityList(List<UserGroupRequestDTO> requestDTOList) {
		if (CollectionUtils.isEmpty(requestDTOList))
			return new ArrayList<>();

		List<UserGroup> groupList = new ArrayList<>();
		for (UserGroupRequestDTO listValue : requestDTOList) {
			UserGroup groupMaster = dtoToEntity(listValue);
			if (groupMaster != null)
				groupList.add(groupMaster);
		}
		return groupList;
	}
	
	public Optional<UserGroup> entityToEntity(UserGroup req,
			Optional<UserGroup> dBDetails) {

		if (!dBDetails.isPresent())
			return Optional.empty();

		dBDetails.get().setUserGroupId(req.getUserGroupId());		
//		dBDetails.get().setGroupName(req.getGroupName());
		dBDetails.get().setGroupDescription(req.getGroupDescription());	
		
		CompanyGroupMaster groupMaster = new CompanyGroupMaster();		
		groupMaster.setCompanyGroupMasterId(req.getCompanyGroupMaster().getCompanyGroupMasterId());		
		dBDetails.get().setCompanyGroupMaster(groupMaster);		
		
		dBDetails.get().setRemark(req.getRemark());	
		dBDetails.get().setActive(req.isActive());
//		dBDetails.get().setCreatedBy(req.getCreatedBy());
//		dBDetails.get().setCreateDateTime(req.getCreateDateTime());
		dBDetails.get().setLastModifiedBy(req.getLastModifiedBy());
		dBDetails.get().setLastModifiedDateTime(req.getLastModifiedDateTime());					
		return dBDetails;
	}
	
	public UserGroupHistory entityToEntityHistory(UserGroup dBDetails,
			UserGroupHistory history) {

		history.setUserGroupId(dBDetails.getUserGroupId());		
		history.setGroupName(dBDetails.getGroupName());
		history.setGroupDescription(dBDetails.getGroupDescription());		
		history.setCompanyGroupMasterId(dBDetails.getCompanyGroupMaster().getCompanyGroupMasterId());			
		history.setRemark(dBDetails.getRemark());	
		history.setActive(dBDetails.isActive());
		history.setCreatedBy(dBDetails.getCreatedBy());
		history.setCreatedDateTime(dBDetails.getCreatedDateTime());
		history.setLastModifiedBy(dBDetails.getLastModifiedBy());
		history.setLastModifiedDateTime(dBDetails.getLastModifiedDateTime());
		return history;
	}
	
	public UserGroupRequestBean dtoToBean(UserGroupRequestDTO dto) {

		if (dto == null)
			return new UserGroupRequestBean();
		UserGroupRequestBean master = new UserGroupRequestBean();
		
//		master.setUserGroupId(dto.getUserGroupId());		
		master.setGroupName(dto.getGroupName());
		master.setGroupDescription(dto.getGroupDescription());	
		
//		CompanyGroupMaster groupMaster = new CompanyGroupMaster();		
//		groupMaster.setCompanyGroupMasterId(dto.getCompanyGroupMasterId());
//		master.setCompanyGroupMaster(groupMaster);
		
		master.setCompanyGroupIds(dto.getCompanyGroupMasterIdList());
		master.setRemark(dto.getRemark());	
		master.setActive(dto.isActive());
		master.setCreatedBy(dto.getCreatedBy());
//		master.setCreatedDateTime(StringUtils.stringToDate(dto.getCreatedDateTime()));
//		master.setLastModifiedBy(dto.getLastModifiedBy());
//		master.setLastModifiedDateTime(StringUtils.stringToDate(dto.getLastModifiedDateTime()));
		return master;
	}
	
	
	public UserGroupDTO beanToDto(UserGroupBean bean) {

		UserGroupDTO dto = new UserGroupDTO();		
		dto.setUserGroupId(bean.getUserGroupId());		
		dto.setGroupName(bean.getGroupName());	
		dto.setGroupDescription(bean.getGroupDescription());
		dto.setCompanyGroupMasterId(bean.getCompanyGroupMasterId());			
		dto.setActive(bean.isActive());				
		return dto;
	}
	
	public List<UserGroupDTO> beanListToDtoList(List<UserGroupBean> beanList) {
		List<UserGroupDTO> responseDTOList = new ArrayList<UserGroupDTO>();
		beanList.forEach(s -> {
			responseDTOList.add(beanToDto(s));			
		});
		return responseDTOList;
	}
	
}
