package com.ps.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.error.handler.NoContentFoundException;
import com.ps.RESTful.error.handler.RequestNotAcceptable;
import com.ps.RESTful.error.handler.ResourceAlreadyExistException;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.EmployeeRoleAssingment;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.entities.master.UserRole;
import com.ps.services.EmployeeRoleAssingmentService;
import com.ps.services.dao.repository.master.CompanyGroupMasterRepository;
import com.ps.services.dao.repository.master.CompanyMasterRepository;
import com.ps.services.dao.repository.master.EmployeeRoleAssingmentRepository;
import com.ps.services.dao.repository.master.GlobalUserMasterRepository;
import com.ps.services.dao.repository.master.UserRoleRepository;
import com.ps.util.RequestUtils;

@Service
public class EmployeeRoleAssingmentServiceImpl implements EmployeeRoleAssingmentService {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	EmployeeRoleAssingmentRepository empRoleAssignmentRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	GlobalUserMasterRepository globalUserMasterRepository;

	@Autowired
	CompanyMasterRepository globalCompanyMasterRepository;

	@Autowired
	CompanyGroupMasterRepository companyGroupRepository;

	@Autowired
	RequestUtils requestUtils;

	/*
	 * Fetch-All-Employee-Role-Assignment-Details-by-UserRoleId
	 */
	@Override
	public List<EmployeeRoleAssingment> getMenuesForRoleID(int userRoleId) {

		if (logger.isDebugEnabled())
			logger.debug("In getMenuesForRoleID Method from EmployeeRoleAssingment Service");

		// Check-UserRole-Exist?
		Optional<UserRole> existUserRole = userRoleRepository.findById(userRoleId);

		if (!existUserRole.isPresent()) {
			if (logger.isDebugEnabled())
				logger.debug("Requested UserRole is not exist. Searched by Id:" + userRoleId);

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Requested UserRole is not exist. Searched by Id:" + userRoleId);

		}

		// Collect-EmployeeRoles-having-requested-UserRole
		List<EmployeeRoleAssingment> existEmployeeWithUserRole = empRoleAssignmentRepository.findAll().stream()
				.filter(emp -> emp.getUserRoles().getUserRoleId() == userRoleId).collect(Collectors.toList());

		if (existEmployeeWithUserRole.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug(
						"No Information is exist for Employee having UserRole:" + existUserRole.get().getRoleName());

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Information not exist of Employee's having UserRole:" + existUserRole.get().getRoleName());

		}

		return existEmployeeWithUserRole;
	}// getMenuesForRoleID-Close

	/*
	 * Fetch-All-Employee-Role-Assignment-Details-by-CompanyId
	 */
	@Override
	public List<EmployeeRoleAssingment> getMenuesForCompanyId(int companyId) {
		if (logger.isDebugEnabled())
			logger.debug("In getMenuesForCompanyId Method from EmployeeRoleAssingment Service");

		// Check-Company-Exist?
		Optional<GlobalCompanyMaster> existCompanyDetails = globalCompanyMasterRepository.findById(companyId);

		if (!existCompanyDetails.isPresent()) {
			if (logger.isDebugEnabled())
				logger.debug("Requested Company is not exist. Searched by Id:" + companyId);

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Requested Company is not exist. Searched by Id:" + companyId);

		}

		// Collect-EmployeeRoles-having-requested-CompanyId
		List<EmployeeRoleAssingment> existEmployeeWithCompanyId = empRoleAssignmentRepository.findAll().stream()
				.filter(emp -> emp.getGlobalCompanyMaster().getGlobalCompanyMasterId() == companyId)
				.collect(Collectors.toList());

		if (existEmployeeWithCompanyId.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("No Information is exist for Employee having Company:"
						+ existCompanyDetails.get().getCompanyName());

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Information not exist of Employee's having Company:" + existCompanyDetails.get().getCompanyName());

		}

		return existEmployeeWithCompanyId;
	}// getMenuesForCompanyId-Close

	/*
	 * Fetch-All-Employee-Role-Assignment-Details-by-CompanyGroupId
	 */
	@Override
	public List<EmployeeRoleAssingment> getEmployeeforCompanyGroupId(int companyGroupId) {

		if (logger.isDebugEnabled())
			logger.debug("In getEmployeeforCompanyGroupId Method from EmployeeRoleAssingment Service");

		// Check-CompanyGroup-Exist?
		Optional<CompanyGroupMaster> existCompanyGroup = companyGroupRepository.findById(companyGroupId);

		if (!existCompanyGroup.isPresent()) {
			if (logger.isDebugEnabled())
				logger.debug("Requested Company Group is not exist. Searched by Id:" + companyGroupId);

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Requested Company Group is not exist. Searched by Id:" + companyGroupId);

		}

		// Collect-EmployeeRoles-having-requested-companyGroupId
		List<EmployeeRoleAssingment> existEmployeeWithCompanyId = empRoleAssignmentRepository.findAll().stream()
				.filter(emp -> emp.getCompanyGroupMaster().getCompanyGroupMasterId() == companyGroupId)
				.collect(Collectors.toList());

		if (existEmployeeWithCompanyId.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("No Information is exist for Employee having Company Group:"
						+ existCompanyGroup.get().getCompanyGroupName());

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Information not exist of Employee's having Company Group:"
							+ existCompanyGroup.get().getCompanyGroupName());
		}

		return existEmployeeWithCompanyId;
	}// getMenuesForUserGroupId-Close

	/*
	 * Add-New-EmployeeRoleAssignment-Details
	 */
	@Override
	public List<EmployeeRoleAssingment> addNewEmployeeRoleAssignment(
			List<EmployeeRoleAssingment> employeeAssginmentDetails) {

		if (logger.isDebugEnabled())
			logger.debug("In addNewEmployeeRoleAssignment Method from EmployeeRoleAssingment Service");

		// Hold-All-User-Details-will-used-for-Searching
		List<GlobalUserMaster> allUserDetails = globalUserMasterRepository.findAll();

		// Hold-All-CompanyGroup-Details-will-used-for-Searching
		List<CompanyGroupMaster> allExsitCompnayGroup = companyGroupRepository.findAll();

		// Hold-All-Company-Details-will-used-for-Searching
		List<GlobalCompanyMaster> allCompanyDetails = globalCompanyMasterRepository.findAll();

		// Hold-All-UserRole-Details-will-used-for-Searching
		List<UserRole> allExistUserRoles = userRoleRepository.findAll();

		// Hold-All-EmployeeRoleAssignmentDetails-to-check-duplication-of-records
		List<EmployeeRoleAssingment> allExistEmployeeRoleAssignment = empRoleAssignmentRepository.findAll();

		// Save-unique-list
		List<EmployeeRoleAssingment> uniqueRecords = new ArrayList<EmployeeRoleAssingment>();

		/*
		 * Perform-Basic-Validations
		 */
		for (EmployeeRoleAssingment singleRecord : employeeAssginmentDetails) {

			if (logger.isDebugEnabled())
				logger.debug("Performing-Validations for add new EmployeeRoleAssignment Request:" + singleRecord);

			// Check-GlobalUserMasterId-exist?
			Optional<GlobalUserMaster> existUserDetails = allUserDetails.stream()
					.filter(user -> user.getUserMasterId() == singleRecord.getGlobalUserMaster().getUserMasterId())
					.findFirst();

			if (existUserDetails.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.error("Global User Master details not found. Searched by Id:"
							+ singleRecord.getGlobalUserMaster().getUserMasterId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT, "Requested User not found!.Searched by Id:"
						+ singleRecord.getGlobalUserMaster().getUserMasterId());

			} else {
				if (!existUserDetails.get().isActive()) {
					if (logger.isDebugEnabled())
						logger.error("User:" + singleRecord.getGlobalUserMaster().getUserName()
								+ " is not active in state. Can not process request.");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
							"User:" + singleRecord.getGlobalUserMaster().getUserName() + " is not Active.");
				}
			}

			// Check-CompanyGroup-exist-and-active?
			Optional<CompanyGroupMaster> existCompanyGroup = allExsitCompnayGroup.stream()
					.filter(companyGroup -> companyGroup.getCompanyGroupMasterId() == singleRecord
							.getCompanyGroupMaster().getCompanyGroupMasterId())
					.findFirst();
			String companyGroupCode;
			if (existCompanyGroup.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.error("Company Group Master details not found. Searched by Id:"
							+ singleRecord.getCompanyGroupMaster().getCompanyGroupMasterId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested Company Group not found!. Searched by Id:"
								+ singleRecord.getCompanyGroupMaster().getCompanyGroupMasterId());

			} else {
				if (existCompanyGroup.get().isActive() == false) {
					if (logger.isDebugEnabled())
						logger.error("Company Group:" + singleRecord.getCompanyGroupMaster().getCompanyGroupName()
								+ " is not active. Can not process request.");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE, "Company Group:"
							+ singleRecord.getCompanyGroupMaster().getCompanyGroupName() + " is not Active.");
				} else {
					// its-Active
					// Now-Save-CompanyGroupCode
					companyGroupCode = new String(existCompanyGroup.get().getCompanyGroupCode().trim().toUpperCase());
				}
			}

			// Check-Company-exist-and-active?
			Optional<GlobalCompanyMaster> existCompanyDetails = allCompanyDetails.stream().filter(company -> company
					.getGlobalCompanyMasterId() == singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId())
					.findFirst();
			if (existCompanyDetails.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.error("Company details not found. Searched by Id:"
							+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested Company not found!. Searched by Id:"
								+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

			} else {
				if (!existCompanyDetails.get().isActive()) {
					if (logger.isDebugEnabled())
						logger.error("Company :" + singleRecord.getGlobalCompanyMaster().getCompanyName()
								+ " is not active. Can not process request.");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
							"Company :" + singleRecord.getGlobalCompanyMaster().getCompanyName()
									+ " is not active. Can not process request.");
				}

				/*
				 * 1- Check-Requested-Company-belongs-to-Requested-companyGroup?. 2-
				 * Check-CompanyGroupCode-with-companyGroupName-of-gloablCompanyMaster-Details.
				 */

				if (!companyGroupCode
						.equalsIgnoreCase(existCompanyDetails.get().getCompanyGroupCode().trim().toUpperCase())) {

					if (logger.isDebugEnabled())
						logger.error("Requested Company:" + existCompanyDetails.get().getCompanyName()
								+ " does not belongs to CompanyGroup:" + existCompanyGroup.get().getCompanyGroupName());

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
							"Requested Company:" + existCompanyDetails.get().getCompanyName()
									+ " does not belongs to CompanyGroup:"
									+ existCompanyGroup.get().getCompanyGroupName());
				}

			}

			// Check-Requested-UserRole-exist?
			Optional<UserRole> existUserRole = allExistUserRoles.stream()
					.filter(role -> role.getUserRoleId() == singleRecord.getUserRoles().getUserRoleId()).findFirst();

			if (existUserRole.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.error("User Role details not found. Searched by Id:"
							+ singleRecord.getUserRoles().getUserRoleId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested User Role not found!. Searched by Id:"
								+ singleRecord.getUserRoles().getUserRoleId());
			} else {

				if (!existUserRole.get().isActive()) {
					if (logger.isDebugEnabled())
						logger.error("User Role :" + existUserRole.get().getRoleName()
								+ " is not active. Can not process request.");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE, "User Role :"
							+ existUserRole.get().getRoleName() + " is not active. Can not process request.");
				}

				// Now-Check-Requested-UseeRole-and-its-UserGroup-belongs-to-CompanyGroup?
				if (existUserRole.get().getUserGroup().getCompanyGroupMaster()
						.getCompanyGroupMasterId() != existCompanyGroup.get().getCompanyGroupMasterId()) {
					if (logger.isDebugEnabled())
						logger.error("User Role :" + singleRecord.getUserRoles().getRoleName()
								+ " does not belongs to the CompanyGroup:"
								+ existCompanyGroup.get().getCompanyGroupName());

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
							"User Role :" + existUserRole.get().getRoleName() + " does not belongs to the CompanyGroup:"
									+ existCompanyGroup.get().getCompanyGroupName());

				}

			}

			// Check-Requested-information-is-already-exist?
			Optional<EmployeeRoleAssingment> duplicateRecord = allExistEmployeeRoleAssignment.stream()
					.filter(assgingment -> assgingment.getGlobalUserMaster().getUserMasterId() == singleRecord
							.getGlobalUserMaster().getUserMasterId()
							&& assgingment.getCompanyGroupMaster().getCompanyGroupMasterId() == singleRecord
									.getCompanyGroupMaster().getCompanyGroupMasterId()
							&& assgingment.getGlobalCompanyMaster().getGlobalCompanyMasterId() == singleRecord
									.getGlobalCompanyMaster().getGlobalCompanyMasterId()
							&& assgingment.getUserRoles().getUserRoleId() == singleRecord.getUserRoles()
									.getUserRoleId())
					.findFirst();

			if (duplicateRecord.isPresent()) {
				if (logger.isDebugEnabled())
					logger.warn("User:" + existUserDetails.get().getUserName() + " alrady has Access to Company Group:"
							+ existCompanyGroup.get().getCompanyGroupName() + " for Company:"
							+ existCompanyDetails.get().getCompanyName() + " with Role:"
							+ existUserRole.get().getRoleName() + ". Skip record from being add into unique list");

			} else {
				// No-duplicate-record-add-into-unique-list
				singleRecord.setIsActive(true);
				singleRecord.setCreatedBy(requestUtils.getUserName());

				uniqueRecords.add(singleRecord);
			}

		} // for-Closes

		// Check-is-unique-list-is-empty?
		if (uniqueRecords.isEmpty()) {

			if (logger.isDebugEnabled())
				logger.warn("Requested Employee Role Assignment are already exist!");

			throw new ResourceAlreadyExistException(ErrorCode.ALREADY_EXIST,
					"Requested Employee Role Assignment are already exist!");
		}

		// Save-Details
		List<EmployeeRoleAssingment> savedDetails = new ArrayList<EmployeeRoleAssingment>();

		try {

			savedDetails = empRoleAssignmentRepository.saveAll(uniqueRecords);
			return savedDetails;

		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.error("Failed to save the Employee Role assignment Details. Exception:" + e.getMessage());

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Failed to save Employee Role Assignment Detail. Contact Admin Team.");

		}

	}// addNewEmployeeRoleAssignment-Close

	/*
	 * Update-Existing-EmployeeRoleAssignment-Details
	 */
	@Override
	public List<EmployeeRoleAssingment> updateEmployeeRoleAssignment(
			List<EmployeeRoleAssingment> employeeAssginmentDetails) {

		if (logger.isDebugEnabled())
			logger.debug("In updateEmployeeRoleAssignment Method from EmployeeRoleAssingment Service");

		// Hold-All-User-Details-will-used-for-Searching
		List<GlobalUserMaster> allUserDetails = globalUserMasterRepository.findAll();

		// Hold-All-CompanyGroup-Details-will-used-for-Searching
		List<CompanyGroupMaster> allExsitCompnayGroup = companyGroupRepository.findAll();

		// Hold-All-Company-Details-will-used-for-Searching
		List<GlobalCompanyMaster> allCompanyDetails = globalCompanyMasterRepository.findAll();

		// Hold-All-UserRole-Details-will-used-for-Searching
		List<UserRole> allExistUserRoles = userRoleRepository.findAll();

		// Hold-All-EmployeeRoleAssignmentDetails-to-check-duplication-of-records
		List<EmployeeRoleAssingment> allExistEmployeeRoleAssignment = empRoleAssignmentRepository.findAll();

		// Save-unique-list
		List<EmployeeRoleAssingment> recordsToUpdate = new ArrayList<EmployeeRoleAssingment>();

		/*
		 * Perform-Basic-Validations
		 */
		for (EmployeeRoleAssingment singleRecord : employeeAssginmentDetails) {

			if (logger.isDebugEnabled())
				logger.debug("Performing-Validations to Update EmployeeRoleAssignment Request:" + singleRecord);

			// Check-Requested-Record-already-exist?
			Optional<EmployeeRoleAssingment> existEmployeeRole = allExistEmployeeRoleAssignment.stream()
					.filter(emp -> emp.getEmployeeRoleAssignmentId() == singleRecord.getEmployeeRoleAssignmentId())
					.findFirst();

			if (existEmployeeRole.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Employee Role Assignment not found!. Searched by Id:"
							+ singleRecord.getEmployeeRoleAssignmentId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested Employee Role Assignment Details not exist!. Searched By Id:"
								+ singleRecord.getEmployeeRoleAssignmentId());

			}

			// Check-Request-is-to-DeActive-Record
			if (existEmployeeRole.get().getIsActive() == true && singleRecord.getIsActive() == false) {
				if (logger.isDebugEnabled())
					logger.debug("Request is to De-Activat Record:" + existEmployeeRole.get());

				// Set-up
				existEmployeeRole.get().setIsActive(false);
				existEmployeeRole.get().setLastModifiedBy(requestUtils.getUserName());

				// Add-into-upgrade-list
				recordsToUpdate.add(existEmployeeRole.get());

			} else {
				if (logger.isDebugEnabled())
					logger.debug("Request is to change some information");

				// Check-GlobalUserMasterId-exist?
				Optional<GlobalUserMaster> existUserDetails = allUserDetails.stream()
						.filter(user -> user.getUserMasterId() == singleRecord.getGlobalUserMaster().getUserMasterId())
						.findFirst();

				if (existUserDetails.isEmpty()) {
					if (logger.isDebugEnabled())
						logger.error("Global User Master details not found. Searched by Id:"
								+ singleRecord.getGlobalUserMaster().getUserMasterId());

					throw new NoContentFoundException(SuccessCode.NO_CONTENT,
							"Requested User not found!.Searched by Id:"
									+ singleRecord.getGlobalUserMaster().getUserMasterId());

				} else {
					if (!existUserDetails.get().isActive()) {
						if (logger.isDebugEnabled())
							logger.error("User:" + existUserDetails.get().getUserName()
									+ " is not active in state. Can not process request.");

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
								"User:" + existUserDetails.get().getUserName() + " is not Active.");
					}
				}

				// Check-CompanyGroup-exist-and-active?
				Optional<CompanyGroupMaster> existCompanyGroup = allExsitCompnayGroup.stream()
						.filter(companyGroup -> companyGroup.getCompanyGroupMasterId() == singleRecord
								.getCompanyGroupMaster().getCompanyGroupMasterId())
						.findFirst();
				String companyGroupCode;
				if (existCompanyGroup.isEmpty()) {
					if (logger.isDebugEnabled())
						logger.error("Company Group Master details not found. Searched by Id:"
								+ singleRecord.getCompanyGroupMaster().getCompanyGroupMasterId());

					throw new NoContentFoundException(SuccessCode.NO_CONTENT,
							"Requested Company Group not found!. Searched by Id:"
									+ singleRecord.getCompanyGroupMaster().getCompanyGroupMasterId());

				} else {
					if (existCompanyGroup.get().isActive() == false) {
						if (logger.isDebugEnabled())
							logger.error("Company Group:" + existCompanyGroup.get().getCompanyGroupName()
									+ " is not active. Can not process request.");

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
								"Company Group:" + existCompanyGroup.get().getCompanyGroupName() + " is not Active.");
					} else {
						// its-Active
						// Now-Save-CompanyGroupCode
						companyGroupCode = new String(
								existCompanyGroup.get().getCompanyGroupCode().trim().toUpperCase());
					}
				}

				// Check-Company-exist-and-active?
				Optional<GlobalCompanyMaster> existCompanyDetails = allCompanyDetails.stream().filter(company -> company
						.getGlobalCompanyMasterId() == singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId())
						.findFirst();
				if (existCompanyDetails.isEmpty()) {
					if (logger.isDebugEnabled())
						logger.error("Company details not found. Searched by Id:"
								+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

					throw new NoContentFoundException(SuccessCode.NO_CONTENT,
							"Requested Company not found!. Searched by Id:"
									+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

				} else {
					if (!existCompanyDetails.get().isActive()) {
						if (logger.isDebugEnabled())
							logger.error("Company :" + existCompanyDetails.get().getCompanyName()
									+ " is not active. Can not process request.");

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
								"Company :" + existCompanyDetails.get().getCompanyName()
										+ " is not active. Can not process request.");
					}

					/*
					 * 1- Check-Requested-Company-belongs-to-Requested-companyGroup?. 2-
					 * Check-CompanyGroupCode-with-companyGroupName-of-gloablCompanyMaster-Details.
					 */

					if (!companyGroupCode
							.equalsIgnoreCase(existCompanyDetails.get().getCompanyGroupCode().trim().toUpperCase())) {

						if (logger.isDebugEnabled())
							logger.error("Requested Company:" + existCompanyDetails.get().getCompanyName()
									+ " does not belongs to CompanyGroup:"
									+ existCompanyGroup.get().getCompanyGroupName());

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
								"Requested Company:" + existCompanyDetails.get().getCompanyName()
										+ " does not belongs to CompanyGroup:"
										+ existCompanyGroup.get().getCompanyGroupName());
					}

				}

				// Check-Requested-UserRole-exist?
				Optional<UserRole> existUserRole = allExistUserRoles.stream()
						.filter(role -> role.getUserRoleId() == singleRecord.getUserRoles().getUserRoleId())
						.findFirst();

				if (existUserRole.isEmpty()) {
					if (logger.isDebugEnabled())
						logger.error("User Role details not found. Searched by Id:"
								+ singleRecord.getUserRoles().getUserRoleId());

					throw new NoContentFoundException(SuccessCode.NO_CONTENT,
							"Requested User Role not found!. Searched by Id:"
									+ singleRecord.getUserRoles().getUserRoleId());
				} else {

					if (!existUserRole.get().isActive()) {
						if (logger.isDebugEnabled())
							logger.error("User Role :" + existUserRole.get().getRoleName()
									+ " is not active. Can not process request.");

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE, "User Role :"
								+ existUserRole.get().getRoleName() + " is not active. Can not process request.");
					}

					// Now-Check-Requested-UseeRole-and-its-UserGroup-belongs-to-CompanyGroup?
					if (existUserRole.get().getUserGroup().getCompanyGroupMaster()
							.getCompanyGroupMasterId() != existCompanyGroup.get().getCompanyGroupMasterId()) {
						if (logger.isDebugEnabled())
							logger.error("User Role :" + existUserRole.get().getRoleName()
									+ " does not belongs to the CompanyGroup:"
									+ existCompanyGroup.get().getCompanyGroupName());

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
								"User Role :" + existUserRole.get().getRoleName()
										+ " does not belongs to the CompanyGroup:"
										+ existCompanyGroup.get().getCompanyGroupName());

					}

				}

				/*
				 * Everything-is-ok0Ready-to-Update-as-requested
				 * Set-new-Details-and-existing-record
				 */

				existEmployeeRole.get().setGlobalUserMaster(existUserDetails.get());
				existEmployeeRole.get().setCompanyGroupMaster(existCompanyGroup.get());
				existEmployeeRole.get().setGlobalCompanyMaster(existCompanyDetails.get());
				existEmployeeRole.get().setUserRoles(existUserRole.get());
				existEmployeeRole.get().setLastModifiedBy(requestUtils.getUserName());
				existEmployeeRole.get().setIsActive(true);

				recordsToUpdate.add(existEmployeeRole.get());

			} // else-close-of-checkReqOfDeactivation

		} // for-Closes

		// Check-is-recordsToUpdate-list-is-empty?
		if (recordsToUpdate.isEmpty()) {

			if (logger.isDebugEnabled())
				logger.warn("Non of Records are being filtered for updation.");

			throw new ResourceAlreadyExistException(ErrorCode.ALREADY_EXIST,
					"Non of Records are identifited for updation request");
		}

		// Save-Details
		List<EmployeeRoleAssingment> savedDetails = new ArrayList<EmployeeRoleAssingment>();

		try {

			savedDetails = empRoleAssignmentRepository.saveAll(recordsToUpdate);
			return savedDetails;

		} catch (Exception e) {
			if (logger.isDebugEnabled())
				logger.error("Failed to save the Employee Role assignment Details. Exception:" + e.getMessage());

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Failed to save Employee Role Assignment Detail. Contact Admin Team.");

		}
	}// updateEmployeeRoleAssignment-close

	/*
	 * Fetch-Employee-role-assignment-details-by-userId(glbalUserMasterId)
	 */
	@Override
	public List<EmployeeRoleAssingment> getAssignmentDetailsByUserId(int globalUserMasterId) {

		if (logger.isDebugEnabled())
			logger.debug("In getAssignmentDetailsByUserId Method from EmployeeRoleAssingment Service ");

		// Fetch-all-details-for-requested-Id
		List<EmployeeRoleAssingment> assignmentDetails = empRoleAssignmentRepository.findAll().stream()
				.filter(emp -> emp.getGlobalUserMaster().getUserMasterId() == globalUserMasterId)
				.collect(Collectors.toList());

		if (assignmentDetails.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Assignment Details list is empty");

			throw new NoContentFoundException(SuccessCode.NO_CONTENT, "Assignment Details list is empty");
		} else {

			if (logger.isDebugEnabled())
				logger.debug("Total:" + assignmentDetails.size() + " Assignment Details found");

			return assignmentDetails;
		}

	}// getAssignmentDetailsByUserId-Close

}
