package com.ps.RESTful.resources.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ps.RESTful.dto.mapper.GlobalUserMasterDTOMapper;
import com.ps.RESTful.dto.mapper.JwtDTOMapper;
import com.ps.RESTful.dto.request.GlobalUserMasterRequestDTO;
import com.ps.RESTful.dto.response.GlobalUserMasterResponseDTO;
import com.ps.RESTful.dto.response.JwtResponseDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.resources.GlobalUserMasterResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.RESTful.security.JwtService;
import com.ps.RESTful.security.JwtToken;
import com.ps.beans.JWTBean;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.services.CompanyMasterService;
import com.ps.services.GroupDBMasterService;
import com.ps.services.GlobalUserMasterService;
import com.ps.util.MethodValidationUtils;
import com.ps.util.RequestUtils;

@RestController
@RequestMapping(path = { GlobalUserMasterResource.RESOURCE_PATH, GlobalUserMasterResource.RESOURCE_PATH_WITH_COMPANY })
public class GlobalUserMasterResourceImpl extends AbstractResourceImpl implements GlobalUserMasterResource {

	Logger logger = Logger.getLogger(GlobalUserMasterResourceImpl.class);

	@Autowired
	GlobalUserMasterDTOMapper userDTOMapper;

	@Autowired
	GlobalUserMasterService userService;

	@Autowired
	RequestUtils requestUtils;

	@Autowired
	CompanyMasterService companyMasterService;

	@Autowired
	GroupDBMasterService groupCompanyMasterService;

	@Autowired
	JwtService jwtService;

	@Autowired
	JwtDTOMapper jwtDTOMapper;

	@Override
	public ResponseEntity<Response> add(GlobalUserMasterRequestDTO requestDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In add user resource from GlobalUserMaster Resource");

		// Check-Id
		MethodValidationUtils.checkIfIdIsZero(requestDTO.getGlobalCompanyMasterId(), "globalCompanyMasterId");

		if (logger.isDebugEnabled())
			logger.debug(
					"In add user resource " + "adding user for company-> " + requestDTO.getGlobalCompanyMasterId());

		GlobalCompanyMaster company = companyMasterService.getById(requestDTO.getGlobalCompanyMasterId());

		if (logger.isDebugEnabled())
			logger.debug("Mapping dto to entity");

		GlobalUserMaster user = userDTOMapper.dtoToEntity(requestDTO);
		user.setGlobalCompanyMaster(company);

		/*
		 * This comment by MayurG user.setGroupCompany(company.getGroupCompany());
		 */

		if (logger.isDebugEnabled())
			logger.debug(
					"Calling user service " + "to add user entry into db for userName-> " + requestDTO.getUserName());
		userService.add(user);

		if (logger.isDebugEnabled())
			logger.debug("User added Returning response from User resource for " + user.getUserName());

		return ResponseEntity.status(HttpStatus.OK).body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "User added Successfully").build());
	}// add-User

	@Override
	public ResponseEntity<Response> addAll(int companyId, List<GlobalUserMasterRequestDTO> requestDTOList) {

		if (logger.isDebugEnabled())
			logger.debug("In addAll user resource ");

		List<GlobalUserMaster> usersList = new ArrayList<GlobalUserMaster>();

		if (!CollectionUtils.isEmpty(requestDTOList)) {
			if (logger.isDebugEnabled())
				logger.debug("Iterating over the list of users for mapping" + " them into entities totalUsers-> "
						+ requestDTOList.size());

			for (GlobalUserMasterRequestDTO userDTO : requestDTOList) {
				GlobalUserMaster user = userDTOMapper.dtoToEntity(userDTO);
				if (user != null) {

					if (logger.isDebugEnabled())
						logger.debug("Fnding company with id-> " + companyId + " in database");

					GlobalCompanyMaster company = companyMasterService.getById(companyId);
					user.setGlobalCompanyMaster(company);
					/*
					 * This commment by MayurG user.setGroupCompany(company.getGroupCompany());
					 */
					usersList.add(user);
				}
			}
		}

		if (logger.isDebugEnabled())
			logger.debug("Calling user service for validating and adding users into database");

		userService.addAll(usersList);

		if (logger.isDebugEnabled())
			logger.debug("Users added, returning response from User resource");

		return ResponseEntity.status(HttpStatus.OK).body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "User records added Successfully")
				.build());
	}// AddAll-close

	@Override
	public ResponseEntity<Response> verify(GlobalUserMasterRequestDTO requestDTO, String type) {

		if (logger.isDebugEnabled())
			logger.debug("In verify Endpoint of user resource");

		GlobalUserMaster user = null;
		SuccessCode successCode = SuccessCode.OK;

		if (type.equalsIgnoreCase("email")) {
			if (logger.isDebugEnabled())
				logger.debug("Calling service method to verify user email received in request " + "email-> "
						+ requestDTO.getEmailId());
			user = userService.verifyUserByEmail(requestDTO.getEmailId());

		} else if (type.equalsIgnoreCase("mobile")) {
			if (logger.isDebugEnabled())
				logger.debug("Calling service method to verify user mobile received in request " + "mobile-> "
						+ requestDTO.getMobileNumber());
			user = userService.verifyUserByMobile(requestDTO.getMobileNumber());

			if (StringUtils.isBlank(user.getEmailId()))
				successCode = SuccessCode.VERIFIED_USER_ONLY_MOBILE;

		} else {
			if (type.equalsIgnoreCase("employeeDetails")) {
				if (logger.isDebugEnabled())
					logger.debug("Calling service method to verify user employeeDetails received in request ");
				userService.verifyUserByMobileAndEmployeeDetails(requestDTO.getMobileNumber(),
						requestDTO.getEmployeeDetails());
			}
		}

		GlobalUserMasterResponseDTO userResponseDTO = userDTOMapper.entityToDto(user);

		if (logger.isDebugEnabled())
			logger.debug("User email-> " + requestDTO.getEmailId() + "" + " or mobile-> " + requestDTO.getMobileNumber()
					+ " verified succesfully, returning response from User resource");
		return ResponseEntity.status(HttpStatus.OK).body(ResponseBuilder.builder().result(userResponseDTO)
				.status(StatusEnum.SUCCESS.getValue(), successCode.getCode(), "User details verified successfully.")
				.build());
	}

	@Override
	public ResponseEntity<Response> setResetPassword(int resourceId, GlobalUserMasterRequestDTO requestDTO,
			String action) {

		if (logger.isDebugEnabled())
			logger.debug("In set/reset Password EP of users resource");

		GlobalUserMaster user = userService.getById(resourceId);

		if (logger.isDebugEnabled())
			logger.debug("User is valid and present in the database userId-> " + user.getUserMasterId());

		if (action.equalsIgnoreCase("set")) {
			if (StringUtils.isBlank(user.getPassword())) {
				if (logger.isDebugEnabled())
					logger.debug("setting password -> " + requestDTO.getPassword()
							+ " into user object with userName-> " + user.getUserName());
				user.setPassword(requestDTO.getPassword());
			}

			userService.setPassword(user);
		} else {
			if (action.equalsIgnoreCase("reset")) {
				userService.resetPassword(user, requestDTO.getPassword());
			}
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Password is set Successfully")
						.build());
	}

	@Override
	public ResponseEntity<Response> login(GlobalUserMasterRequestDTO requestDTO) {
		if (logger.isDebugEnabled())
			logger.debug("Mapping dto to entity");
		GlobalUserMaster user = userDTOMapper.dtoToEntity(requestDTO);

		user = userService.authenticate(user);

		GlobalUserMasterResponseDTO responseDTO = userDTOMapper.entityToDto(user);

		if (logger.isDebugEnabled())
			logger.debug("OTP Generated For:" + responseDTO.getUserName());

		/*
		 * Removing-responseDTO-as-its not required.
		 * 
		 * return ResponseEntity.status(HttpStatus.CREATED)
		 * .body(ResponseBuilder.builder() .status(StatusEnum.SUCCESS.getValue(),
		 * SuccessCode.OK.getCode(), "OTP sent successfully")
		 * .result(responseDTO).build());
		 */

		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "OTP sent successfully").build());
	}

	@Override
	public ResponseEntity<Response> getById(int resourceId) {

		if (logger.isDebugEnabled())
			logger.debug("In getById method from Global User Master Resource");

		// Check-Id-is-valid?
		MethodValidationUtils.checkIfIdIsZero(resourceId, "resourceId");

		// Call-to-Service-method
		GlobalUserMaster userDetails = userService.getById(resourceId);

		// Convert-to-DTO
		GlobalUserMasterResponseDTO responseDTO = userDTOMapper.entityToDto(userDetails);

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "User details found")
						.result(responseDTO).build());
	}// getById-Close

	/*
	 * This-will-Re-Generate-JWT-Token
	 * This-request-will-fetch-userDetails-from-passed-JWT-Token-(It-Must-be-Active/
	 * Valid/Yet-toExpire-and-then-Re-Generate-the-token-using-the-information-
	 * available-in-passed-JWT
	 */
	@Override
	public ResponseEntity<Response> reGenerateJWTToken() {

		if (logger.isDebugEnabled())
			logger.debug("In refreshJWTToken method from Global User Master Resource");

		// Call-to-Service-method
		GlobalUserMaster userDetails = userService.getById(Integer.parseInt(jwtService.getClaims().getSubject()));

		if (logger.isDebugEnabled())
			logger.debug("Re-Generating Token for User:" + userDetails.getEmailId());

		JwtToken tokenString = jwtService.createLoginToken(userDetails);

		// Set-to-Bean
		JWTBean jwtBean = new JWTBean();

		if (tokenString != null) {
			jwtBean.setToken(tokenString.getToken());
		} else {
			if (logger.isDebugEnabled())
				logger.debug("Failed to Re-Generate Token for User:" + userDetails.getEmailId());

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to Re-Generate Token");
		}

		// Convert-to-responseDTO
		JwtResponseDTO responseDTO = jwtDTOMapper.beanToDTO(jwtBean);

		return ResponseEntity.status(HttpStatus.CREATED).body(ResponseBuilder.builder()
				.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Token Re-Generated successfully")
				.result(responseDTO).build());
	}// reGenerateJWTToken

	@Override
	public ResponseEntity<Response> getByCompanyId(int resourceId) {

		if (logger.isDebugEnabled())
			logger.debug("In getByCompanyId method from Global User Master Resource");

		// Check-Id-is-valid?
		MethodValidationUtils.checkIfIdIsZero(resourceId, "resourceId");

		// Call-to-Service-method
		List<GlobalUserMaster> userDetails = userService.getUsersByCompanyId(resourceId);

		
		// Convert-to-DTO
		List<GlobalUserMasterResponseDTO> responseDTOList = new ArrayList<GlobalUserMasterResponseDTO>();
		
		for (GlobalUserMaster globalUserMaster : userDetails) {
			GlobalUserMasterResponseDTO responseDTO = userDTOMapper.entityToDto(globalUserMaster);
			responseDTOList.add(responseDTO);
		}

		return ResponseEntity.status(HttpStatus.OK)
				.body(ResponseBuilder.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "User details found")
						.results(responseDTOList).build());

	}// getByCompanyId-Close

}
