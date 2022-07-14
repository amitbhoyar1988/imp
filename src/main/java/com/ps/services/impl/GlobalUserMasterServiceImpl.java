package com.ps.services.impl;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.IncorrectResultSizeDataAccessException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import com.ps.RESTful.dto.request.EmployeeMasterRequestDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.BusinessException;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.error.handler.NoContentFoundException;
import com.ps.RESTful.error.handler.ResourceAlreadyExistException;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.RESTful.properties.AbstractProperties;
import com.ps.dto.EmailDTO;
import com.ps.dto.ErrorDTO;
import com.ps.entities.master.EmployeeRoleAssingment;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.entities.master.PasswordPolicy;
import com.ps.entities.master.UserPasswordHistory;
import com.ps.services.EmailService;
import com.ps.services.EmployeeService;
import com.ps.services.GlobalUserMasterService;
import com.ps.services.OTPDetailsService;
import com.ps.services.PasswordPolicyService;
import com.ps.services.UserPasswordHistoryService;
import com.ps.services.dao.repository.master.CompanyMasterRepository;
import com.ps.services.dao.repository.master.EmployeeRoleAssingmentRepository;
import com.ps.services.dao.repository.master.GlobalUserMasterRepository;
import com.ps.util.MessageTemplateConstants;
import com.ps.util.PasswordPolicyUtils;
import com.ps.util.RegExValidationUtils;
import com.ps.util.RequestUtils;
import com.ps.util.SMSSenderUtils;
import com.ps.util.URLEncoderUtils;

@Service
public class GlobalUserMasterServiceImpl implements GlobalUserMasterService {

	Logger logger = Logger.getLogger(GlobalUserMasterServiceImpl.class);

	@Autowired
	AbstractProperties abstractProperties;

	@Autowired
	SMSSenderUtils smsSenderUtils;

	@Autowired
	UserPasswordHistoryService passwordHistoryService;

	@Autowired
	CompanyMasterRepository globalCompanyMasterRepository;

	@Autowired
	GlobalUserMasterRepository userMasterRepository;

	@Autowired
	EmployeeRoleAssingmentRepository employeeRoleAssignmentRepository;

	@Autowired
	PasswordPolicyService passwordPolicyService;

	@Autowired
	EmailService emailService;

	@Autowired
	EmployeeService employeeService;

	@Autowired
	OTPDetailsService otpDetailService;

	@Autowired
	BCryptPasswordEncoder bcryptEncoder;

	@Autowired
	RequestUtils requestUtils;

	@Override
	public void add(GlobalUserMaster user) {

		if (logger.isDebugEnabled())
			logger.debug("In user service add method");

		ErrorDTO error = validate(user);
		if (error != null) {
			throw new InvalidRequestException(error.getCode(), error.getMessage());
		}

		if (user.getCreateDate() == null) {
			Date today = new Date();
			if (logger.isDebugEnabled())
				logger.debug("Created date is not present in the request so setting todays date-> " + today);
			user.setCreateDate(today);
		}

		/*
		 * Before-searching-was-like-below:EmailId-Or-modileNumber-is-exist-then-will-be
		 * -treated-as-already-exist Optional<GlobalUserMaster> existingUser =
		 * userMasterRepository.findByEmailIdOrMobileNumberAndIsActive(user.getEmailId()
		 * , user.getMobileNumber(), true);
		 * 
		 */

		/*
		 * MayurG:- Check-EmailId-AND-Mobile-number-is-exist-then-user-is-already-exist.
		 */
		Optional<GlobalUserMaster> existingUser = userMasterRepository
				.findByEmailIdAndMobileNumberAndIsActive(user.getEmailId(), user.getMobileNumber(), true);

		if (existingUser.isPresent()) {
			if (logger.isDebugEnabled())
				logger.debug("User with emailId-> " + user.getEmailId() + ", And mibile-> " + user.getMobileNumber()
						+ " already exists in Database");
			throw new ResourceAlreadyExistException(ErrorCode.ALREADY_EXIST, "User already Exists!");
		}

		if (logger.isDebugEnabled())
			logger.debug("User object is valid, saving into database");

		// Saving-into-Database
		try {

			user.setCreatedBy(requestUtils.getUserName());
			userMasterRepository.save(user);

		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.debug("failed saving User into database" + e.getMessage());

			throw new BusinessException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save User Details.");

		}

		if (logger.isDebugEnabled())
			logger.debug("User added Successfully, will be sending set password link through email/sms");
		sendSetPasswordLink(user);
	}

	@Override
	public void addAll(List<GlobalUserMaster> usersList) {

		if (logger.isDebugEnabled())
			logger.debug("In user service addAll method");

		if (CollectionUtils.isEmpty(usersList))
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Users list is Invalid!");

		List<GlobalUserMaster> validUsers = new ArrayList<GlobalUserMaster>();
		List<GlobalUserMaster> invalidUsers = new ArrayList<GlobalUserMaster>();

		if (logger.isDebugEnabled())
			logger.debug("Iterating over list of users to validate them and segregate " + "invalid and valid users");

		List<String> emailList = new ArrayList<String>();
		List<String> mobileList = new ArrayList<String>();

		usersList.forEach((user) -> {

			if (logger.isDebugEnabled())
				logger.debug("Validating user-> " + user.getUserName());
			ErrorDTO error = validate(user);
			if (error != null) {
				if (logger.isDebugEnabled())
					logger.debug("User-> " + user.getFirstName() + " is invalid, adding into invalid users list "
							+ "error message-> " + error.getMessage() + " code-> " + error.getCode());
				invalidUsers.add(user);
				return; // only skips this iteration.
			}

			if (emailList.contains(user.getEmailId())) {
				if (logger.isDebugEnabled())
					logger.debug(
							"User-> " + user.getEmailId() + " is duplicate in request not adding user with firstName-> "
									+ user.getFirstName() + " in valid Users list");
				invalidUsers.add(user);
				return;
			}

			if (mobileList.contains(user.getMobileNumber())) {
				if (logger.isDebugEnabled())
					logger.debug("User-> " + user.getMobileNumber()
							+ " is duplicate in request not adding user with firstName-> " + user.getFirstName()
							+ " in valid Users list");
				invalidUsers.add(user);
				return;
			}

			emailList.add(user.getEmailId());
			mobileList.add(user.getMobileNumber());

			if (logger.isDebugEnabled())
				logger.debug("User-> " + user.getUserName() + " is valid, adding into valid users list");

			user.setCreatedBy(requestUtils.getUserName());
			validUsers.add(user);

		});

		if (logger.isDebugEnabled())
			logger.debug("Total users-> " + usersList.size() + ", valid users-> " + validUsers.size()
					+ ", invalid users-> " + invalidUsers.size());

		if (usersList.size() == invalidUsers.size()) {
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User details are invalid, could not add users!");
		}

		userMasterRepository.saveAll(validUsers);

		if (logger.isDebugEnabled())
			logger.debug("After successfully adding valid users into the database, sending set-password link for each");
		sendSetPasswordLinkBatch(validUsers);
	}

	@Override
	public GlobalUserMaster getById(int id) {

		if (logger.isDebugEnabled())
			logger.debug("In user getById service method, finding user with id-> " + id);
		if (id <= 0)
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User Id is invalid");

		Optional<GlobalUserMaster> user = userMasterRepository.findById(id);

		if (!user.isPresent())
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, "User not found!");

		if (!user.get().isActive())
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, "User is not Active!");

		if (user.get().isLocked())
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, "User is blocked!");

		return user.get();
	}

	@Override
	public GlobalUserMaster verifyUserByEmail(String emailId) {

		if (logger.isDebugEnabled())
			logger.debug("In verifyUserByEmail emailId-> " + emailId);
		ErrorDTO error = validateEmailOrMobile(emailId, null);
		if (error != null)
			throw new InvalidRequestException(error.getCode(), error.getMessage());

		if (logger.isDebugEnabled())
			logger.debug("In verifyUserByEmail finding user for emailId-> " + emailId);
		Optional<GlobalUserMaster> userOptional = userMasterRepository.findActiveByEmailId(emailId);

		if (!userOptional.isPresent())
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User not found!");

		if (userOptional.get().isLocked())
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User is blocked!");

		if (logger.isDebugEnabled())
			logger.debug("Found user in database with mobile-> " + emailId + ", user master id is-> "
					+ userOptional.get().getUserMasterId());

		sendResetPasswordLink(userOptional.get());

		return userOptional.get();
	}

	@Override
	public GlobalUserMaster verifyUserByMobile(String mobile) {

		if (logger.isDebugEnabled())
			logger.debug("In verifyUserByMobile mobile-> " + mobile);
		ErrorDTO error = validateEmailOrMobile(null, mobile);
		if (error != null)
			throw new InvalidRequestException(error.getCode(), error.getMessage());

		if (logger.isDebugEnabled())
			logger.debug("User mobile-> " + mobile + " is valid, finding user in database");
		Optional<GlobalUserMaster> userOptional = userMasterRepository.findActiveByMobileNumber(mobile);
		// isEmpty-was-there-changedBy-MayurG
		if (!userOptional.isPresent())
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User not found!");

		if (userOptional.get().isLocked())
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User is blocked!");

		if (logger.isDebugEnabled())
			logger.debug("Found user in database with mobile-> " + mobile + ", user master id is-> "
					+ userOptional.get().getUserMasterId());

		if (StringUtils.isBlank(userOptional.get().getEmailId()))
			sendResetPasswordLink(userOptional.get());

		return userOptional.get();
	}

	@Override
	public GlobalUserMaster verifyUserByMobileAndEmployeeDetails(String mobile, EmployeeMasterRequestDTO employeeDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In verifyUserByMobileAndEmployeeDetails mobile-> " + mobile);

		GlobalUserMaster user = verifyUserByMobile(mobile);

		if (employeeDTO == null)
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Employee details are Invalid");

		if (logger.isDebugEnabled())
			logger.debug(
					"In verifyUserByMobileAndEmployeeDetails before calling employee verify service  EmployeeCode-> "
							+ employeeDTO.getEmployeeCode() + "DateOfBirth()-> " + employeeDTO.getDateOfBirth());
		// call employee service
		boolean isVerified = employeeService.verifyEmployeeDetails(employeeDTO, user.getGroupDBMaster().getGroupName());

		if (isVerified)
			sendResetPasswordLink(user);

		return user;
	}

	@Override
	@Transactional("masterTransactionManager")
	public void setPassword(GlobalUserMaster user) {

		if (logger.isDebugEnabled())
			logger.debug("In setPassword, checking if user is null");
		if (user == null)
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Invalid user!");

		if (logger.isDebugEnabled())
			logger.debug("In setPassword method setting password for userID-> " + user.getUserMasterId());
		if (StringUtils.isBlank(user.getPassword()))
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password is Invalid!");

		if (logger.isDebugEnabled())
			logger.debug("Getting password policy for companyId-> "
					+ user.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		PasswordPolicy passwordPolicy = passwordPolicyService
				.getByCompanyId(user.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		if (logger.isDebugEnabled())
			logger.debug("Password policy for companyId-> " + user.getGlobalCompanyMaster().getGlobalCompanyMasterId()
					+ " is policyId-> " + passwordPolicy.getId());

		Date today = new Date();

		// setting next password change date based on expiry in days set in password
		// policy
		if (logger.isDebugEnabled())
			logger.debug("Setting next password changeDate time to " + passwordPolicy.getExpiryInDays() + " days "
					+ "from today-> " + today);
		Calendar passwordPolicyExpiry = Calendar.getInstance();
		passwordPolicyExpiry.add(Calendar.DATE, +passwordPolicy.getExpiryInDays());
		user.setNextPasswordChangeDateTime(passwordPolicyExpiry.getTime());
		if (logger.isDebugEnabled())
			logger.debug(
					"Next password changeDate time to " + user.getNextPasswordChangeDateTime() + ", today-> " + today);

		// setting password due date based on due in days set in password policy
		if (logger.isDebugEnabled())
			logger.debug("Setting password due date time to " + passwordPolicy.getDueInDays() + " days "
					+ "from today-> " + today);
		Calendar passwordPolicyDueDate = Calendar.getInstance();
		passwordPolicyDueDate.add(Calendar.DATE, +passwordPolicy.getDueInDays());
		user.setPasswordDueDateTime(passwordPolicyDueDate.getTime());
		if (logger.isDebugEnabled())
			logger.debug("Password Due Date time is " + user.getPasswordDueDateTime() + ", today-> " + today);

		validatePasswordByPolicy(user, passwordPolicy);
		userMasterRepository.save(user);
	}

	@Override
	@Transactional("masterTransactionManager")
	public void resetPassword(GlobalUserMaster user, String newPassword) {

		if (logger.isDebugEnabled())
			logger.debug("In resetPassword, checking if user is null");
		if (user == null)
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Invalid user!");

		if (logger.isDebugEnabled())
			logger.debug("In resetPassword method validating user password-> " + user.getPassword());
		if (StringUtils.isBlank(user.getPassword()))
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST,
					"Password is not set for user, Please set a password");

		if (logger.isDebugEnabled())
			logger.debug("Getting password policy for companyId-> "
					+ user.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		PasswordPolicy passwordPolicy = passwordPolicyService
				.getByCompanyId(user.getGlobalCompanyMaster().getGlobalCompanyMasterId());

		user.setPassword(newPassword);
		validatePasswordByPolicy(user, passwordPolicy);

		userMasterRepository.save(user);
	}

	private void validatePasswordByPolicy(GlobalUserMaster user, PasswordPolicy passwordPolicy) {

		if (logger.isDebugEnabled())
			logger.debug("Calling passwordPolicy utils method to validate password policy");
		List<String> message = PasswordPolicyUtils.isValid(user, passwordPolicy);

		if (!CollectionUtils.isEmpty(message))
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, message.toString());

		if (logger.isDebugEnabled())
			logger.debug("Getting password history list for user with id-> " + user.getUserMasterId());
		List<UserPasswordHistory> passwordHistoryList = passwordHistoryService.getAllByUserId(user.getUserMasterId());

		if (!CollectionUtils.isEmpty(passwordHistoryList)) {

			if (logger.isDebugEnabled())
				logger.debug("Total password history for user with id-> " + user.getUserMasterId() + " is -> "
						+ passwordHistoryList.size());
			UserPasswordHistory oldestUserPassword = null;

			if (logger.isDebugEnabled())
				logger.debug("Iterating over list to check if new password already exists for user with id-> "
						+ user.getUserMasterId() + " total passwords-> " + passwordHistoryList.size());
			for (UserPasswordHistory userPasswordHistory : passwordHistoryList) {

				if (oldestUserPassword == null || oldestUserPassword.getId() > userPasswordHistory.getId()) {
					oldestUserPassword = userPasswordHistory;
				}
				if (logger.isDebugEnabled())
					logger.debug("Old password id-> " + userPasswordHistory.getId() + " new password for userId-> "
							+ user.getUserMasterId());

				if (bcryptEncoder.matches(user.getPassword(), userPasswordHistory.getPassword())) {
					if (logger.isDebugEnabled())
						logger.debug("Old password id-> " + userPasswordHistory.getId()
								+ " matches new password set for userId-> " + user.getUserMasterId()
								+ ", returning error response");
					throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password shouldn't match old passwords!");
				}

			}

			if (passwordHistoryList.size() >= passwordPolicy.getAllowedPasswordHistorycount()
					&& oldestUserPassword != null) {
				if (logger.isDebugEnabled())
					logger.debug("Total pasword history records-> " + passwordHistoryList.size()
							+ " is more than alowed history count-> "
							+ passwordPolicy.getAllowedPasswordHistorycount());
				passwordHistoryService.deleteById(oldestUserPassword.getId());
			}
		}

		user.setLastPasswordChangeDateTime(new Date());
		user.setPassword(bcryptEncoder.encode(user.getPassword()));
		setPasswordHistory(user);
	}

	private void setPasswordHistory(GlobalUserMaster user) {

		UserPasswordHistory passwordHistory = new UserPasswordHistory();
		passwordHistory.setUserMaster(user);
		passwordHistory.setPassword(user.getPassword());

		if (logger.isDebugEnabled())
			logger.debug("Adding password history into database for user-> " + user.getUserMasterId());
		passwordHistoryService.add(passwordHistory);
	}

	@Override
	public GlobalUserMaster authenticate(GlobalUserMaster user) {

		// validate
		ErrorDTO errorDTO = validateEmailOrMobile(user.getEmailId(), user.getMobileNumber());
		if (errorDTO != null) {
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Enter valid email id or mobile number");
		}

		/*
		 * List<GlobalUserMaster> userOptional = userMasterRepository
		 * .findAllByEmailIdOrMobileNumberAndIsActive(user.getEmailId(),
		 * user.getMobileNumber(), true);
		 * 
		 * if (logger.isDebugEnabled()) logger.debug("Matching Data:" +
		 * userOptional.toString());
		 * 
		 * //Check-List-is-empty? if (userOptional.isEmpty()) { throw new
		 * InvalidRequestException(ErrorCode.UNAUTHORIZED,
		 * "User not found! Check your credentials"); }
		 */

		/*
		 * Fetch-User-details-by-EmailId-OR-MobileNumber
		 */
		Optional<GlobalUserMaster> userInfo = Optional.empty();

		try {
			userInfo = userMasterRepository.findByEmailIdOrMobileNumberAndIsActive(user.getEmailId(),
					user.getMobileNumber(), true);

		} catch (Exception e) {
			// TODO: handle exception
			if (logger.isDebugEnabled())
				logger.error("Failed to Fetch user Details:" + e.getMessage() + " Class:" + e.getClass().toString());

			if (e.getClass().equals(IncorrectResultSizeDataAccessException.class)) {
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED,
						"Multiple record exist with similar information!");

			} else {
				throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to serach User!");
			}
		}

		// Set-as-Effectively-final-variable
		Optional<GlobalUserMaster> userData = userInfo;

		if (userData.isEmpty() || !bcryptEncoder.matches(user.getPassword(), userData.get().getPassword())) {

			if (logger.isDebugEnabled())
				logger.debug("Unable to login: user not exist OR password is incorrect");

			throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Invalid userId or password");
		}

		// Check-User-has-any-role-assigned?
		List<EmployeeRoleAssingment> employeeRoles = employeeRoleAssignmentRepository.findAll().stream()
				.filter(roles -> roles.getGlobalUserMaster().getUserMasterId() == userData.get().getUserMasterId()
						&& roles.getIsActive() == true)
				.collect(Collectors.toList());

		if (employeeRoles.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Requested User:" + userData.get().getUserName()
						+ " does not have any role assigned Or Assigned role is De-Activated. Refusing access");

			throw new InvalidRequestException(ErrorCode.UNAUTHORIZED,
					"User:" + userData.get().getUserName() + " not have permission to login!");
		}

		if (userData.get().getNextPasswordChangeDateTime() != null) {
			Date today = new Date();
			if (logger.isDebugEnabled())
				logger.debug("Next password date-time is-> " + userData.get().getNextPasswordChangeDateTime()
						+ "current date is " + today + " for userId-> " + userData.get().getUserMasterId());
			if (today.compareTo(userData.get().getNextPasswordChangeDateTime()) > 0) {

				if (logger.isDebugEnabled())
					logger.debug("Sending Rest Password Link");

				// SendingRestPasswordLink
				sendResetPasswordLink(userData.get());

				throw new InvalidRequestException(ErrorCode.REDIRECT_REQUEST, "Password has Expired!");
			}
		} else {
			if (logger.isDebugEnabled())
				logger.error("Next password change date time is null");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("Generating OTP and Saving");
		}

		boolean otpStatus = otpDetailService.generateAndSaveOTP(userData.get());

		// Check-OTP-Sent-Status
		if (otpStatus == true) {

			if (logger.isDebugEnabled())
				logger.debug("OTP Sent:" + otpStatus);

			return userData.get();

		} else {

			if (logger.isDebugEnabled())
				logger.debug("OTP Sent:" + otpStatus);

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Failed to Generate/Sent OTP. Contact Your Admin");
		}

	}// Authentication-Close

	private ErrorDTO validate(GlobalUserMaster user) {

		if (logger.isDebugEnabled())
			logger.debug("In user service isValid method, checking if user is null");

		if (user == null)
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Invalid user!");

		if (logger.isDebugEnabled())
			logger.debug("In user service isValid method " + " validating user " + user.getUserName());

		if (user.getGlobalCompanyMaster() == null) {
			if (logger.isDebugEnabled())
				logger.debug("Company is null for user-> " + user.getUserName());
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Company is Invalid!");
		}

		if (StringUtils.isBlank(user.getFirstName())) {
			if (logger.isDebugEnabled())
				logger.debug("User firstname is -> " + user.getUserName());
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Users' first name is Invalid!");
		}

		if (StringUtils.isBlank(user.getLastName())) {
			if (logger.isDebugEnabled())
				logger.debug("User last name is -> " + user.getUserName());
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "Users' last name is Invalid!");
		}

		if (user.getActivateDate() == null) {
			if (logger.isDebugEnabled())
				logger.debug("Checking if activate date for user-> " + user.getFirstName()
						+ "  is valid activateDate-> " + user.getActivateDate());
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "ActivateDate is Invalid!");
		}

		if (StringUtils.isBlank(user.getMobileNumber())) {
			if (logger.isDebugEnabled())
				logger.debug("In valid activate date for user-> " + user.getUserName() + "  email-> "
						+ user.getActivateDate());
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "ActivateDate is Invalid!");
		}

		ErrorDTO emailOrMobileError = validateEmailOrMobile(user.getEmailId(), user.getMobileNumber());
		if (emailOrMobileError != null) {
			return emailOrMobileError;
		}

		return null;
	}

	private ErrorDTO validateEmailOrMobile(String emailId, String mobile) {

		if ((StringUtils.isBlank(emailId)) && (StringUtils.isBlank(mobile))) {
			if (logger.isDebugEnabled())
				logger.debug("Both User email or mobile is not present  email-> " + emailId + " mobile-> " + mobile);
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "User Email/Mobile is Invalid!");
		}

		if ((!StringUtils.isBlank(emailId)) && (!RegExValidationUtils.isValidEmail(emailId))) {
			if (logger.isDebugEnabled())
				logger.debug("Invalid Email for user--> " + emailId + "  email-> " + emailId);
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "User email is Invalid!");
		}

		if ((!StringUtils.isBlank(mobile)) && (!RegExValidationUtils.isValidMobile(mobile))) {
			if (logger.isDebugEnabled())
				logger.debug("Invalid Mobile for user--> " + mobile + "  email-> " + mobile);
			return new ErrorDTO(ErrorCode.BAD_REQUEST, "User mobile number is Invalid!");
		}

		return null;
	}

	private void sendResetPasswordLink(GlobalUserMaster user) {

		if (logger.isDebugEnabled())
			logger.debug("In send password link method, checking if user is null");
		if (user == null)
			return;

		if (logger.isDebugEnabled())
			logger.debug("In send password link method, sending link for user-> " + user.getUserName());
		if (!StringUtils.isBlank(user.getMobileNumber())) {
			getMobileLink(user.getMobileNumber(), "resetPassword");
		}

		if (!StringUtils.isBlank(user.getEmailId())) {
			emailService.send(createEmailRequest(user.getEmailId(), MessageTemplateConstants.RESET_PASSWORD_SUBJECT,
					getEmailLink(user.getEmailId(), "resetPassword")));
		}
	}

	private void sendSetPasswordLink(GlobalUserMaster user) {

		if (logger.isDebugEnabled())
			logger.debug("In send password link method, checking if user is null");
		if (user == null)
			return;

		if (logger.isDebugEnabled())
			logger.debug("In send password link method, sending link for user-> " + user.getUserName());

		if (!StringUtils.isBlank(user.getMobileNumber())) {
			if (logger.isDebugEnabled())
				logger.debug("Sending set password link on mobile-> " + user.getMobileNumber());
			smsSenderUtils.send(getMobileLink(user.getMobileNumber(), "setPassword"), user.getMobileNumber());
		}

		if (!StringUtils.isBlank(user.getEmailId())) {
			if (logger.isDebugEnabled())
				logger.debug("Sending set password link on email-> " + user.getEmailId());
			emailService.send(createEmailRequest(user.getEmailId(), MessageTemplateConstants.SET_PASSWORD_SUBJECT,
					getEmailLink(user.getEmailId(), "setPassword")));
		}
	}

	private void sendSetPasswordLinkBatch(List<GlobalUserMaster> users) {

		if (logger.isDebugEnabled())
			logger.debug("In send password link method, sending link in batch");

		if (CollectionUtils.isEmpty(users))
			return;

		List<GlobalUserMaster> mobileUsers = new ArrayList<GlobalUserMaster>();
		List<EmailDTO> emailUsers = new ArrayList<EmailDTO>();

		users.forEach((user) -> {

			if (!StringUtils.isBlank(user.getMobileNumber())) {
				mobileUsers.add(user);
			}

			if (!StringUtils.isBlank(user.getEmailId())) {
				if (logger.isDebugEnabled())
					logger.debug("Building set password link for email-> " + user.getEmailId()
							+ ", and adding it to list for sending in batch");
				emailUsers.add(createEmailRequest(user.getEmailId(), MessageTemplateConstants.SET_PASSWORD_SUBJECT,
						getEmailLink(user.getEmailId(), "setPassword")));
			}
		});

		emailService.sendAll(emailUsers);

		// call sms batch service to send sms
		mobileUsers.forEach(user -> {
			if (logger.isDebugEnabled())
				logger.debug("Sending set password link for mobile-> " + user.getMobileNumber());
			smsSenderUtils.send(getMobileLink(user.getMobileNumber(), "setPassword"), user.getMobileNumber());
		});
	}

	private String getEmailLink(String email, String type) {

		if (logger.isDebugEnabled())
			logger.debug("In getMobileLink method building " + type + " link for email->" + email);

		String body = "";
		String encodedEmailId = URLEncoderUtils.encodeValue(email);

		if (type.equalsIgnoreCase("setPassword")) {
			String link = abstractProperties.getUi().getSetPasswordLink() + "&email=" + encodedEmailId;
			body = String.format(MessageTemplateConstants.SET_PASSWORD_MESSAGE_BODY, link);
		} else {
			String link = abstractProperties.getUi().getResetPasswordLink() + "&email=" + encodedEmailId;
			body = String.format(MessageTemplateConstants.RESET_PASSWORD_MESSAGE_BODY, link);
		}

		if (logger.isDebugEnabled())
			logger.debug("Email set/reset password link for email-> " + email + " MessageBody-> " + body);
		return body;
	}

	private String getMobileLink(String mobile, String type) {

		if (logger.isDebugEnabled())
			logger.debug("In getMobileLink method building " + type + " link for mobile->" + mobile);

		String body = "";
		String encodedMobile = URLEncoderUtils.encodeValue(mobile);

		if (type.equalsIgnoreCase("setPassword")) {
			String link = abstractProperties.getUi().getSetPasswordLink() + "&mobile=" + encodedMobile;
			body = String.format(MessageTemplateConstants.SET_PASSWORD_MESSAGE_BODY, link);
		} else {
			String link = abstractProperties.getUi().getResetPasswordLink() + "&mobile=" + encodedMobile;
			body = String.format(MessageTemplateConstants.RESET_PASSWORD_MESSAGE_BODY, link);
		}

		if (logger.isDebugEnabled())
			logger.debug("Mobile set/reset password link for mobile-> " + mobile + " MessageBody-> " + body);
		return body;
	}

	private EmailDTO createEmailRequest(String to, String subject, String body) {

		if (StringUtils.isBlank(to))
			return null;

		EmailDTO email = new EmailDTO();
		email.setTo(to);
		email.setSubject(subject);
		email.setBody(body);

		return email;
	}

	@Override
	public List<GlobalUserMaster> getUsersByCompanyId(int id) {

		if (logger.isDebugEnabled())
			logger.debug("In user getById service method, finding user with id-> " + id);

		if (id <= 0)
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "User Id is invalid");

		// Check-Requested-Id-exist?
		Optional<GlobalCompanyMaster> existCompany = globalCompanyMasterRepository.findById(id);

		if (!existCompany.isPresent()) {
			if (logger.isDebugEnabled())
				logger.debug("Company with an id:" + id);

			throw new NoContentFoundException(SuccessCode.NO_CONTENT, "Requested Company with id:" + id + " not found!");
		}

		// Search-User-by-Company-Id
		List<GlobalUserMaster> user = userMasterRepository.findAll().stream()
				.filter(users -> users.getGlobalCompanyMaster().getGlobalCompanyMasterId() == id)
				.collect(Collectors.toList());

		if (user.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Users not exist for company:" + existCompany.get().getCompanyName());

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Users not found for Company:" + existCompany.get().getCompanyName());
		}

		if (logger.isDebugEnabled())
			logger.debug("Total:" + user.size() + " Users found for company:" + existCompany.get().getCompanyName());

		return user;
	}// getUsersByCompanyId-Close

}
