package com.ps.RESTful.dto.mapper;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.ApplicationMenusRequestDTO;
import com.ps.RESTful.dto.response.ApplicationMenusResponseDTO;
import com.ps.entities.master.ApplicationMenus;
import com.ps.util.StringUtils;

@Component
public class ApplicationMenusDTOMapper
		implements AbstractDTOMapper<ApplicationMenusRequestDTO, ApplicationMenusResponseDTO, ApplicationMenus> {

	@Override
	public ApplicationMenus dtoToEntity(ApplicationMenusRequestDTO requestDTO) {

		ApplicationMenus menuDetails = new ApplicationMenus();

		menuDetails.setApplicationMenuId(requestDTO.getApplicationMenuId());
		menuDetails.setParentMenuId(requestDTO.getParentMenuId());
		menuDetails.setMenuName(requestDTO.getMenuName());
		menuDetails.setMenuDescription(requestDTO.getMenuDescription());
		menuDetails.setIsActive(requestDTO.getIsActive());

		menuDetails.setCreatedBy(requestDTO.getCreatedBy());
		menuDetails.setCreatedDateTime(StringUtils.stringToDate(requestDTO.getCreatedDateTime()));
		menuDetails.setLastModifiedBy(requestDTO.getLastModifiedBy());
		menuDetails.setLastModifiedDateTime(StringUtils.stringToDate(requestDTO.getLastModifiedDateTime()));

		return menuDetails;
	}

	@Override
	public ApplicationMenusResponseDTO entityToDto(ApplicationMenus entity) {

		ApplicationMenusResponseDTO responseDTO = new ApplicationMenusResponseDTO();

		responseDTO.setApplicationMenuId(entity.getApplicationMenuId());
		responseDTO.setParentMenuId(entity.getParentMenuId());
		responseDTO.setMenuName(entity.getMenuName());
		responseDTO.setMenuDescription(entity.getMenuDescription());

		responseDTO.setCreatedBy(entity.getCreatedBy());
		responseDTO.setCreatedDateTime(StringUtils.dateToString(entity.getCreatedDateTime()));
		responseDTO.setLastModifiedBy(entity.getLastModifiedBy());
		responseDTO.setLastModifiedDateTime(StringUtils.dateToString(entity.getLastModifiedDateTime()));

		responseDTO.setIsActive(entity.getIsActive());

		return responseDTO;
	}

	public List<ApplicationMenusResponseDTO> entityListToDtoList(List<ApplicationMenus> menuDetails) {

		if (CollectionUtils.isEmpty(menuDetails))
			return new ArrayList<ApplicationMenusResponseDTO>();

		List<ApplicationMenusResponseDTO> responseDTOList = new ArrayList<ApplicationMenusResponseDTO>();

		for (ApplicationMenus listValue : menuDetails) {
			ApplicationMenusResponseDTO departmentDetailsResponseDTO = entityToDto(listValue);
			if (departmentDetailsResponseDTO != null)
				responseDTOList.add(departmentDetailsResponseDTO);
		}
		return responseDTOList;

	}

	public List<ApplicationMenus> dtoListToEntityList(List<ApplicationMenusRequestDTO> requestDTOList) {

		if (CollectionUtils.isEmpty(requestDTOList))
			return new ArrayList<ApplicationMenus>();

		List<ApplicationMenus> detailList = new ArrayList<ApplicationMenus>();

		for (ApplicationMenusRequestDTO listValue : requestDTOList) {
			ApplicationMenus departmentDetails = dtoToEntity(listValue);
			if (departmentDetails != null)
				detailList.add(departmentDetails);
		}
		return detailList;

	}

	public Optional<ApplicationMenus> entityToEntity(ApplicationMenus detailsMenus,
			Optional<ApplicationMenus> detailsMenuDB) {

		if (!detailsMenuDB.isPresent())
			return Optional.empty();

		detailsMenuDB.get().setApplicationMenuId(detailsMenus.getApplicationMenuId());
		detailsMenuDB.get().setParentMenuId(detailsMenus.getParentMenuId());
		detailsMenuDB.get().setMenuName(detailsMenus.getMenuName());
		detailsMenuDB.get().setMenuDescription(detailsMenus.getMenuDescription());
		detailsMenuDB.get().setIsActive(detailsMenus.getIsActive());

		detailsMenuDB.get().setCreatedBy(detailsMenus.getCreatedBy());
		detailsMenuDB.get().setCreatedDateTime(detailsMenus.getCreatedDateTime());
		detailsMenuDB.get().setLastModifiedBy(detailsMenus.getLastModifiedBy());
		detailsMenuDB.get().setLastModifiedDateTime(detailsMenus.getLastModifiedDateTime());
		
		return detailsMenuDB;
	}

}
