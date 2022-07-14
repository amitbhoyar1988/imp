package com.ps.services.impl;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.RESTful.security.JwtServiceImpl;
import com.ps.RESTful.security.JwtToken;
import com.ps.beans.JWTBean;
import com.ps.dto.EmailDTO;
import com.ps.dto.ErrorDTO;
import com.ps.entities.master.AapplicationMaster;
import com.ps.entities.master.EmployeeRoleAssingment;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.entities.master.OTPMaster;
import com.ps.entities.master.OtpFailedTransaction;
import com.ps.services.EmailService;
import com.ps.services.OTPDetailsService;
import com.ps.services.dao.repository.master.AapplicationMasterRepository;
import com.ps.services.dao.repository.master.EmployeeRoleAssingmentRepository;
import com.ps.services.dao.repository.master.GlobalUserMasterRepository;
import com.ps.services.dao.repository.master.OTPDetailsRepository;
import com.ps.services.dao.repository.master.OtpFailedTransactionRepository;
import com.ps.util.RegExValidationUtils;
import com.ps.util.SMSSenderUtils;

@Service
public class OTPDetailsServiceImpl implements OTPDetailsService {

	Logger logger = Logger.getLogger(OTPDetailsServiceImpl.class);

	@Autowired
	SMSSenderUtils smsService;

	@Autowired
	JwtServiceImpl jstService;

	@Autowired
	GlobalUserMasterRepository userDetailsRepositor;

	@Autowired
	EmployeeRoleAssingmentRepository employeeRoleAssignmentRepository;

	@Autowired
	OTPDetailsRepository otpDetailsRepository;

	@Autowired
	OtpFailedTransactionRepository otpFailedTransRepo;

	@Autowired
	Environment env;

	@Autowired
	EmailService emailService;

	@Autowired
	AapplicationMasterRepository applicationMasterRepository;

	@Override
	public boolean generateAndSaveOTP(GlobalUserMaster userData) {
		if (logger.isDebugEnabled())
			logger.debug("generate and save OTP " + "service method saving OTP into database " + userData);

		checkUserStatus(userData);
		// Generate OTP
		String otp = generateOTP();
		boolean status = false;
		// and send OTP to user mobile or email
		String message="Your one time password for Paysquare is : "+otp;
		if (userData.getMobileNumber() != null && userData.getEmailId() != null) {
			status = smsService.send(message, userData.getMobileNumber());
			
			if(logger.isDebugEnabled())
				logger.debug("OTP Send via SMS:"+status);
			
			status = sendEmail(userData, message);
			
			if(logger.isDebugEnabled())
				logger.debug("OTP Send via Email:"+status);
			
		} else if (userData.getMobileNumber() != null) {
			status = smsService.send(message, userData.getMobileNumber());
			
			if(logger.isDebugEnabled())
				logger.debug("OTP Send via SMS:"+status);
			
		} else if (userData.getEmailId() != null) {
			status = sendEmail(userData, message);
			
			if(logger.isDebugEnabled())
				logger.debug("OTP Send via Email:"+status);
			
		}
		OTPMaster otpObj = new OTPMaster();
		otpCreateAndExpiryDateTime(otpObj);
		setOtpValues(otpObj, userData, otp);
		if (status) {
			otpObj.setOtpDeliveryStatus(1);
		} else {
			otpObj.setOtpDeliveryStatus(0);
		}
		// save otp details
		saveOtpAndStatus(otpObj, userData);

		if (logger.isDebugEnabled())
			logger.debug("OTP and it's status saved into database");
		
		return status;
	}

	private boolean sendEmail(GlobalUserMaster userData, String msg) {
		EmailDTO email = new EmailDTO();
		email.setTo(userData.getEmailId());
		email.setSubject("Your one time password find in mail");
		email.setBody(msg);
		emailService.send(email);
		if (logger.isDebugEnabled())
			logger.debug("Sent OTP on Email : " + userData.getEmailId());
		return true;
	}

	private void saveOtpAndStatus(OTPMaster otpObj, GlobalUserMaster userData) {
		List<OTPMaster> otpList = otpDetailsRepository.activeStatusList(userData.getMobileNumber(),
				userData.getEmailId());
		if (!otpList.isEmpty()) {
			otpList.forEach(otpActive -> {
				otpActive.setIsActive(0);
				otpDetailsRepository.save(otpActive);
			});
		}
		otpDetailsRepository.save(otpObj);

		if (logger.isDebugEnabled())
			logger.debug("OTP and it's status saved into database");
	}

	private void checkUserStatus(GlobalUserMaster userData) {
		if (userData.isActive() == false) {
			logger.error("User is not active" + userData.isActive());
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User is not active");
		} else if (userData.isLocked() == true) {

			logger.error("User is locked" + userData.isLocked());
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User is locked");
		}
	}

	private void setOtpValues(OTPMaster otpObj, GlobalUserMaster userData, String otp) {

		List<AapplicationMaster> applicatonDetails = applicationMasterRepository.findAll().stream()
				.filter(app -> app.getEndDate().after(new Date())).collect(Collectors.toList());

		if (applicatonDetails.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Application information not exist Or Application is out of Date.");

			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND,
					"Application Information not exist! Or Application is Out of Date.");
		}

		otpObj.setIsActive(1);
		otpObj.setOtpNumber(Integer.parseInt(otp));

		otpObj.setApplicationId(applicatonDetails.stream().findAny().get().getApplicationMasterId());

		otpObj.setMobileNumber(userData.getMobileNumber());
		otpObj.setEmailId(userData.getEmailId());
		otpObj.setUserId(userData.getUserMasterId());
		otpObj.setIsOTPUsed(0);
		otpObj.setUserOTPAttempt(0);
	}

	private void otpCreateAndExpiryDateTime(OTPMaster otp) {
		LocalDateTime ldt = LocalDateTime.now();
		otp.setCreateDateTime(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
		ldt = ldt.plusMinutes(Integer.parseInt(env.getProperty("otp.expiretime")));
		otp.setExpiryDateTime(Date.from(ldt.atZone(ZoneId.systemDefault()).toInstant()));
	}

	private String generateOTP() {
		if (logger.isDebugEnabled())
			logger.debug("generate OTP");
		Random rnd = new Random();
		int number = rnd.nextInt(999999);
		return String.format("%06d", number);
	}

	@Override
	public JWTBean validateOTP(OTPMaster otpMaster) {

		JWTBean jwtBean = new JWTBean();
		// otp valid
		OTPMaster otpData = otpDetailsRepository.findOTPByEmailIdOrMobile(otpMaster.getEmailId(),
				otpMaster.getMobileNumber());
		// otp expired.
		if (otpData != null && otpData.getOtpNumber() == otpMaster.getOtpNumber()) {
			Date now = new Date();
			if (otpData.getExpiryDateTime().before(now)) {
				logger.error("OTP is expired");
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "OTP is expired");
			}
			// otp already used.
			if (otpData.getIsOTPUsed() == 1) {
				logger.error("OTP is already used");
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "OTP is already used");
			}
			// Is validation attempt more than 3
			int totalAttempts = Integer.parseInt(env.getProperty("otp.attempt.allowed"));
			if (otpData.getUserOTPAttempt() >= totalAttempts) {
				logger.error("Maximum limit exceed");
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Maximum limit exceed");
			}

			/*
			 * Optional<GlobalUserMaster> userOptional = userDetailsRepositor
			 * .findByEmailIdOrMobileNumberAndIsActive(otpMaster.getEmailId(),
			 * otpMaster.getMobileNumber(), true); if (userOptional.isPresent()) { JwtToken
			 * jwt = jstService.createLoginToken(userOptional.get()); if (jwt != null) {
			 * jwtBean.setToken(jwt.getToken()); jwtBean.setUser(userOptional.get());
			 * otpData.setIsOTPUsed(1); otpData.setIsActive(0);
			 * otpDetailsRepository.save(otpData); } else {
			 * logger.error("Invalid User Id and OTP"); throw new
			 * InvalidRequestException(ErrorCode.BAD_REQUEST, "Invalid User Id and OTP"); }
			 * }
			 */

			Optional<GlobalUserMaster> userOptional = userDetailsRepositor
					.findByEmailIdOrMobileNumberAndIsActive(otpMaster.getEmailId(), otpMaster.getMobileNumber(), true);

			// Check-User-has-any-role-assigned?
			List<EmployeeRoleAssingment> employeeRoleDetails = employeeRoleAssignmentRepository.findAll().stream()
					.filter(roles -> roles.getGlobalUserMaster().getUserMasterId() == userOptional.get()
							.getUserMasterId())
					.collect(Collectors.toList());

			if (employeeRoleDetails.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Requested User:" + userOptional.get().getUserName()
							+ " does not have any role assigned. Refusing access");

				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED,
						"User:" + userOptional.get().getUserName() + " not have permission to login!");
			}

			if (!userOptional.isEmpty()) {
				JwtToken jwt = jstService.createLoginToken(userOptional.get());
				if (jwt != null) {
					jwtBean.setToken(jwt.getToken());

					// No-Need-to-include-User-details-into-this-Already-passed-into-Token
					// jwtBean.setUser(userOptional.get());

					otpData.setIsOTPUsed(1);
					otpData.setIsActive(0);
					otpDetailsRepository.save(otpData);
				} else {
					logger.error("Invalid User Id and OTP");
					throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Invalid User Id and OTP");
				}
			}

		} else if (otpData != null) {
			int totalAttempts = Integer.parseInt(env.getProperty("otp.attempt.allowed"));
			if (otpData.getUserOTPAttempt() + 1 >= totalAttempts) {
				lockUser(otpMaster);

				logger.error("Maximum limit exceed your account is locked please contact admin team");
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED,
						"Maximum limit exceed your account is locked please contact admin team");
			}
			otpData.setUserOTPAttempt(otpData.getUserOTPAttempt() + 1);
			otpDetailsRepository.save(otpData);

			if (logger.isDebugEnabled())
				logger.debug("OTP attempt count saved into database: " + otpData.getUserOTPAttempt());

			OtpFailedTransaction failedTransaction = new OtpFailedTransaction();
			failedTransaction.setOtpId(otpData.getOtpId());
			failedTransaction.setUserEnteredOTP(otpMaster.getOtpNumber());
			failedTransaction.setCreateDateTime(new Date());
			otpFailedTransRepo.save(failedTransaction);

			logger.error("Invalid OTP");
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST,
					"Invalid OTP. " + (totalAttempts - otpData.getUserOTPAttempt()) + " attempts remaining.");
		} else {
			logger.error("Provided details are Invalid");
			throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Provided details are Invalid");
		}

		return jwtBean;
	}

	private void lockUser(OTPMaster otpMaster) {
		Optional<GlobalUserMaster> userOptional = userDetailsRepositor
				.findByEmailIdOrMobileNumberAndIsActive(otpMaster.getEmailId(), otpMaster.getMobileNumber(), true);
		if (userOptional.isPresent()) {
			GlobalUserMaster user = userOptional.get();
			user.setLocked(true);
			userDetailsRepositor.save(user);

			if (logger.isDebugEnabled())
				logger.debug("lock user details saved into database: " + user);
		}

	}

	@Override
	public String resend(OTPMaster otpMaster) {
		ErrorDTO error = validateEmailOrMobile(otpMaster.getEmailId(), otpMaster.getMobileNumber());
		if (error != null) {
			logger.error(error.getMessage());
			throw new InvalidRequestException(error.getCode(), error.getMessage());
		}
		Optional<GlobalUserMaster> userOptional = userDetailsRepositor
				.findByEmailIdOrMobileNumberAndIsActive(otpMaster.getEmailId(), otpMaster.getMobileNumber(), true);
		if (!userOptional.isPresent()) {
			logger.error("Provide user deatils are invalid");
			throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Provide user deatils are invalid");
		}
		GlobalUserMaster userData = userOptional.get();
		generateAndSaveOTP(userData);
		if (logger.isDebugEnabled())
			logger.debug("saved generateAndSaveOTP details into database:" + userData);
		return "Otp sent successfully";
	}

	private ErrorDTO validateEmailOrMobile(String emailId, String mobile) {
		ErrorDTO erroDTO = null;
		if ((StringUtils.isBlank(emailId)) && (StringUtils.isBlank(mobile))) {
			if (logger.isDebugEnabled())
				logger.debug("Both User email or mobile is not present  email-> " + emailId + " mobile-> " + mobile);
			erroDTO = new ErrorDTO(ErrorCode.BAD_REQUEST, "User Email/Mobile is Invalid!");
		}
		if (emailId != null) {
			if ((!StringUtils.isBlank(emailId)) && (!RegExValidationUtils.isValidEmail(emailId))) {
				if (logger.isDebugEnabled())
					logger.debug("Invalid Email for user--> " + emailId + "  email-> " + emailId);
				erroDTO = new ErrorDTO(ErrorCode.BAD_REQUEST, "User email is Invalid!");
			}
		}
		if (mobile != null) {
			if ((!StringUtils.isBlank(mobile)) && (!RegExValidationUtils.isValidMobile(mobile))) {
				if (logger.isDebugEnabled())
					logger.debug("Invalid Mobile for user--> " + mobile + "  email-> " + mobile);
				erroDTO = new ErrorDTO(ErrorCode.BAD_REQUEST, "User mobile number is Invalid!");
			}
		}
		return erroDTO;
	}
}
