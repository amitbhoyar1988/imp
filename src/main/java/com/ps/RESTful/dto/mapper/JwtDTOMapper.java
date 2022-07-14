package com.ps.RESTful.dto.mapper;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.ps.RESTful.dto.response.JwtResponseDTO;
import com.ps.beans.JWTBean;

@Component
public class JwtDTOMapper  {

	Logger logger = Logger.getLogger(JwtDTOMapper.class);

	@Autowired
	GlobalUserMasterDTOMapper userDTOMapper;
	
	public JwtResponseDTO beanToDTO(JWTBean jwtBean){
		if(logger.isDebugEnabled()) logger.debug("In JwtDTOMapper mapping beanToDTO");
	
		if(jwtBean == null) return new JwtResponseDTO();
		
		JwtResponseDTO responseDTO =  new JwtResponseDTO();
		
		/*
		 * No-Need-to-add-user-details-into-this-Bean-Already-inside-JWT-token
		 * responseDTO.setUser(userDTOMapper.entityToDto(jwtBean.getUser()));
		 */
		responseDTO.setToken(jwtBean.getToken());
		
		return responseDTO;
	}
	
}
