package com.ps.RESTful.resources.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.ApplicationMenusDTOMapper;
import com.ps.RESTful.dto.response.ApplicatonMenuHierarchyResponseDTO;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.resources.ApplicationMenuDetailsResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.beans.ApplicationMenuHierarchyBean;
import com.ps.services.ApplicationMenuDetailsService;

@RestController
@RequestMapping(path = ApplicationMenuDetailsResource.RESOURCE_PATH)
public class ApplicationMenuDetailsResourceImpl implements ApplicationMenuDetailsResource {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	ApplicationMenusDTOMapper dtoMapper;

	@Autowired
	ApplicationMenuDetailsService menuDetailsService;

	@Override
	public ResponseEntity<Response> getAllMenues() {

		if (logger.isDebugEnabled())
			logger.debug("In GetAllMenues Method from ApplicationMenuDetails Resource");

		// Calling-to-Service-Method
		// List<ApplicationMenuDetails> allMenus = menuDetailsService.getAllMenues();

		List<ApplicationMenuHierarchyBean> allMenus = menuDetailsService.getAllMenues();

		// Convert-Entity-to-DTO
		// List<ApplicationMenuDetailsResponseDTO> responseDTOList =
		// dtoMapper.entityListToDtoList(allMenus);

		// Convert-to-DTO

		List<ApplicatonMenuHierarchyResponseDTO> responseDTOList = new ArrayList<ApplicatonMenuHierarchyResponseDTO>();

		for (ApplicationMenuHierarchyBean iterateBean : allMenus) {
			// ApplicationMenuHierarchyBean bean=new ApplicationMenuHierarchyBean();

			ApplicatonMenuHierarchyResponseDTO responseDTO = iterateBean.beanToDTO(iterateBean);
			responseDTOList.add(responseDTO);
		}

		// Sorting-List-by-parentMenuId
		Collections.sort(responseDTOList);

		return ResponseEntity
				.status(HttpStatus.OK).body(
						ResponseBuilder.builder()
								.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(),
										"Application Menu details found successfully")
								.results(responseDTOList).build());

	}// GetAllMenu-Close

}
