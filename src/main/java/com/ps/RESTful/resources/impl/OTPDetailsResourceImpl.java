package com.ps.RESTful.resources.impl;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.JwtDTOMapper;
import com.ps.RESTful.dto.mapper.OTPDetailsDTOMapper;
import com.ps.RESTful.dto.request.OTPDetailsRequestDTO;
import com.ps.RESTful.dto.response.JwtResponseDTO;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.resources.OTPDetailsResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.beans.JWTBean;
import com.ps.entities.master.OTPMaster;
import com.ps.services.OTPDetailsService;
import com.ps.services.GlobalUserMasterService;

@RestController
@RequestMapping(path = OTPDetailsResource.RESOURCE_PATH)
public class OTPDetailsResourceImpl implements OTPDetailsResource {
	Logger logger = Logger.getLogger(OTPDetailsResourceImpl.class);

	@Autowired
	OTPDetailsService otpValidateService;
	
	@Autowired
	GlobalUserMasterService userService;

	@Autowired
	JwtDTOMapper jwtDTOMapper;

	@Autowired
	OTPDetailsDTOMapper otpDTOMapper;

	@Override
	public ResponseEntity<Response> validate(OTPDetailsRequestDTO otpValidateRequest) {
		if (logger.isDebugEnabled())
			logger.debug("validate OTP details" + "resource");
		if (logger.isDebugEnabled())
			logger.debug("Sending validate OTP details to service method for token genration " + otpValidateRequest);

		OTPMaster master = otpDTOMapper.dtoToEntity(otpValidateRequest);
		JWTBean jwtBean = otpValidateService.validateOTP(master);

		JwtResponseDTO responseDTO = jwtDTOMapper.beanToDTO(jwtBean);

		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Token generated successfully")
						.result(responseDTO).build());
	}

	@Override
	public ResponseEntity<Response> resend(OTPDetailsRequestDTO otpValidateRequest) {
		if (logger.isDebugEnabled())
			logger.debug("validate received OTP details" + "resource");
		if (logger.isDebugEnabled())
			logger.debug(" validate OTP details and resend OTP on Mobile/Email" + otpValidateRequest);
		OTPMaster master = otpDTOMapper.dtoToEntity(otpValidateRequest);
		String message = otpValidateService.resend(master);
		return ResponseEntity.status(HttpStatus.CREATED)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Validate received OTP")
						.result(message).build());
	}
}
