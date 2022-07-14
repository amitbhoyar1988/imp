package com.ps.RESTful.dto.response;

import java.util.List;

import com.ps.beans.ApplicationMenuHierarchyBean;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@ToString
public class ApplicatonMenuHierarchyResponseDTO extends ApplicationMenusResponseDTO{

	//List<ApplicationMenuDetailsResponseDTO> childItems;

	List<ApplicationMenuHierarchyBean> childItems;
	
	//List<ApplicatonMenuHierarchyResponseDTO> childItem;
		
}
