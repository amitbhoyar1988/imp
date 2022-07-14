package com.ps.RESTful.dto.mapper;

import org.jboss.logging.Logger;
import org.springframework.stereotype.Component;

import com.ps.RESTful.dto.request.OTPDetailsRequestDTO;
import com.ps.RESTful.dto.response.OTPDetailsResponseDTO;
import com.ps.entities.master.OTPMaster;

@Component
public class OTPDetailsDTOMapper implements AbstractDTOMapper<OTPDetailsRequestDTO,OTPDetailsResponseDTO, OTPMaster> {
	
	Logger logger = Logger.getLogger(OTPDetailsDTOMapper.class);

	@Override
	public OTPMaster dtoToEntity(OTPDetailsRequestDTO dto) {
		
		if(logger.isDebugEnabled()) logger.debug("In OTPDetailsDTOMapper mapping dto to entity");
		
		if(dto == null) return  new OTPMaster();
		
		OTPMaster master = new OTPMaster();
		master.setEmailId(dto.getEmailId());
		master.setMobileNumber(dto.getMobileNumber());
		master.setOtpNumber(dto.getOtp());
		return master;
	}

	@Override
	public OTPDetailsResponseDTO entityToDto(OTPMaster entity) {
		
		if(logger.isDebugEnabled()) logger.debug("In OTPDetailsDTOMapper mapping entity to dto"); 		

		OTPDetailsResponseDTO dto = new OTPDetailsResponseDTO();
		dto.setEmailId(entity.getEmailId());
		dto.setMobileNumber(entity.getMobileNumber());
		dto.setOtp(entity.getOtpNumber());
		return dto;
		
	}

}
