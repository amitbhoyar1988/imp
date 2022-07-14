package com.ps.beans;

import java.util.HashMap;
import java.util.List;

import com.ps.RESTful.dto.response.ApplicatonMenuHierarchyResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

/**
 * @author MayurG: This Bean will be used to hold the Application Menu Hierarchy
 *         Details. Can be Used for:- 1. Parent to Child relations. 2. Child to
 *         Parent relations.
 */
@Getter
@Setter
@AllArgsConstructor
@ToString
public class ApplicationMenuHierarchyBean {

	private int applicationMenuId;
	private int parentMenuId;
	private String menuName;
	private String menuDescription;
	private Boolean isActive;
	private String createdBy;
	private String createdDateTime;
	private String lastModifiedBy;
	private String lastModifiedDateTime;

	List<ApplicationMenuHierarchyBean> childItems;
	// Set-Possible-accessRights
	HashMap<String, Boolean> possibleAccessRights = new HashMap<String, Boolean>();

	public ApplicatonMenuHierarchyResponseDTO beanToDTO(ApplicationMenuHierarchyBean beanInfo) {
		ApplicatonMenuHierarchyResponseDTO responseDTO = new ApplicatonMenuHierarchyResponseDTO();

		responseDTO.setApplicationMenuId(beanInfo.getApplicationMenuId());
		responseDTO.setParentMenuId(beanInfo.getParentMenuId());
		responseDTO.setMenuName(beanInfo.getMenuName());
		responseDTO.setMenuDescription(beanInfo.getMenuDescription());
		responseDTO.setIsActive(beanInfo.getIsActive());
		responseDTO.setCreatedBy(beanInfo.getCreatedBy());
		responseDTO.setCreatedDateTime(beanInfo.getCreatedDateTime());
		responseDTO.setLastModifiedBy(beanInfo.getLastModifiedBy());
		responseDTO.setLastModifiedDateTime(beanInfo.getLastModifiedDateTime());
		
		// Set-Relations
		responseDTO.setChildItems(beanInfo.getChildItems());

		return responseDTO;
	}

	public ApplicationMenuHierarchyBean() {
		super();
	}
}
