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
import com.ps.entities.master.ApplicationMenus;
import com.ps.entities.master.GlobalCompanyMaster;
import com.ps.entities.master.RolePrivilegesMatrix;
import com.ps.entities.master.UserRole;
import com.ps.services.RolePrivilegesMatrixService;
import com.ps.services.dao.repository.master.ApplicationMenusRepository;
import com.ps.services.dao.repository.master.CompanyMasterRepository;
import com.ps.services.dao.repository.master.RolePrivilegesMatrixRepository;
import com.ps.services.dao.repository.master.UserGroupRepository;
import com.ps.services.dao.repository.master.UserRoleRepository;
import com.ps.util.RequestUtils;

@Service
public class RolePrivilegesMatrixServiceImpl implements RolePrivilegesMatrixService {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	ApplicationMenusRepository applicationMenusRepository;

	@Autowired
	RolePrivilegesMatrixRepository rolePrivilegesMatrixRepository;

	@Autowired
	UserRoleRepository userRoleRepository;

	@Autowired
	UserGroupRepository userGroupRepository;

	@Autowired
	CompanyMasterRepository companyMasterRepository;

	@Autowired
	RequestUtils requestUtils;

	@Override
	public List<RolePrivilegesMatrix> getMenuesForRoleID(int userRoleId) {

		if (logger.isDebugEnabled())
			logger.debug("In getMenuesForRoleID Method from RolePrivilegesMatrix Service");

		// Check-applicationRoleRepository-has-requested-Id?
		Optional<UserRole> userRoleDetails = userRoleRepository.findById(userRoleId);

		if (userRoleDetails.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Requested userRoleId:" + userRoleId + " is not exist");

			throw new NoContentFoundException(SuccessCode.NO_CONTENT, "Requested User Role not exist!");
		}

		// Check-UserRole-is-in-active-State?
		if (userRoleDetails.get().isActive() == false) {
			if (logger.isDebugEnabled())
				logger.debug("Requested userRoleDetails:" + userRoleDetails.get().getRoleName() + " is not active");

			throw new InvalidRequestException(ErrorCode.UNAUTHORIZED,
					"User Role:" + userRoleDetails.get().getRoleName() + " is not active");

		}

		// Get-All-RolePrivilegesMatrix-Data-by-applicationRoleId
		List<RolePrivilegesMatrix> rolePrivilegesMatrixList = rolePrivilegesMatrixRepository.findAll().stream()
				.filter(roles -> roles.getUserRoles().getUserRoleId() == userRoleId)
				.collect(Collectors.toList());

		if (rolePrivilegesMatrixList.isEmpty()) {
			if (logger.isDebugEnabled())
				logger.debug("Role Privileges Matrix data for:" + userRoleId + " is not exist");

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Application Menu Details for Role:" + userRoleDetails.get().getRoleName() + " are not exist!");
		}

		// Get-All-Exist-Menu's
		// List<ApplicationMenuDetails> allExistMenus =
		// applicationMenuDetailsRepository.findAll();

		if (logger.isDebugEnabled())
			logger.debug("Total:" + rolePrivilegesMatrixList.size() + " Menu Privileges are found for Role:"
					+ userRoleDetails.get().getRoleName());

		// Dedicated-Bean-Created-to-hold-all-data-for-RolePrivilegesMatrix
		// List<ApplicationRoleWiseMenuDetailsBean> applicationRoleWiseMenuDetailsBeans
		// = new ArrayList<ApplicationRoleWiseMenuDetailsBean>();

		/*
		 * Loop-to-Set-Details-into-the-bean
		 * 
		 * for (RolePrivilegesMatrix rolePrivilegesMatrix : rolePrivilegesMatrixList) {
		 * 
		 * //Get-his-Parents List<ApplicationMenuHierarchyBean>
		 * parentDetails=getParentMenus(rolePrivilegesMatrix.getApplicationMenus(),
		 * allExistMenus);
		 * 
		 * ApplicationRoleWiseMenuDetailsBean bean = new
		 * ApplicationRoleWiseMenuDetailsBean(rolePrivilegesMatrix,
		 * rolePrivilegesMatrix.getApplicationRoles(), parentDetails);
		 * applicationRoleWiseMenuDetailsBeans.add(bean);
		 * 
		 * } /// for-Close
		 */

		return rolePrivilegesMatrixList;
	}// getMenuesForRoleID-Close

	@Override
	public List<RolePrivilegesMatrix> addPrivileges(List<RolePrivilegesMatrix> requestList) {

		if (logger.isDebugEnabled())
			logger.debug("In addPrivileges Method from RolePrivilegesMatrix Service");

		/*
		 * Perform-basic-validations-for-all-records
		 */

		// Hold-All-exist-menus-to-search.
		List<ApplicationMenus> existMenuDetails = applicationMenusRepository.findAll();

		// Hold-All-Role-privileges-details-to-check-similar-record-request-is-there?
		List<RolePrivilegesMatrix> allExistRolePrivileges = rolePrivilegesMatrixRepository.findAll();

		// Unique-list
		List<RolePrivilegesMatrix> uniqueRecords = new ArrayList<RolePrivilegesMatrix>();

		for (RolePrivilegesMatrix singleRecord : requestList) {

			// Check-Requested-UserRole-is-exist-and-Active?

			Optional<UserRole> existUserRole = userRoleRepository.findById(singleRecord.getUserRoles().getUserRoleId());

			if (existUserRole.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Requested User Role not found! Searched by Id:"
							+ singleRecord.getUserRoles().getUserRoleId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested User Role not found! Searched by Id:" + singleRecord.getUserRoles().getUserRoleId());
			} else {

				// Check-is-it-Active?
				if (existUserRole.get().isActive() == false) {
					if (logger.isDebugEnabled())
						logger.debug("Requested User Role:" + existUserRole + " is InActive");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
							"Requested User Role:" + existUserRole.get().getRoleName() + " is InActive");

				}
			}

			// check-Requested-globalComapny-is-exist-and-active?
			Optional<GlobalCompanyMaster> existGlobalCompany = companyMasterRepository
					.findById(singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

			if (existGlobalCompany.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Requested Global Company not found! Searched by Id:"
							+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested Global Company not found! Searched by Id:"
								+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());
			} else {

				// Check-is-it-Active?
				if (existGlobalCompany.get().isActive() == false) {
					if (logger.isDebugEnabled())
						logger.debug("Requested Global Company:" + existGlobalCompany.get().getCompanyName()
								+ " is InActive");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
							"Requested Company:" + existGlobalCompany.get().getCompanyName() + " is InActive");

				}

			}

			// Check-UserGroup-of-Requested-UserRole-does-belongs-to-the-requested-globalCompany?

			String compnayGroupNameFromGlobalCompany = existGlobalCompany.get().getCompanyGroupCode().trim()
					.toUpperCase();

			/*
			 * This-CompanyGroupName-is-nothing-but-the-companyGroupCode-of-
			 * CompanyGroupMaster
			 */
			if (existUserRole.get().getUserGroup().getCompanyGroupMaster().getCompanyGroupCode().trim().toUpperCase()
					.equalsIgnoreCase(compnayGroupNameFromGlobalCompany)) {
				if (logger.isDebugEnabled())
					logger.debug("Requested User Role Belongs to Company:" + existGlobalCompany.get().getCompanyName());

			} else {
				if (logger.isDebugEnabled())
					logger.debug("Requested User Role:" + existUserRole + " not Belongs to Company:"
							+ existGlobalCompany.get().getCompanyName());

				throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
						"Requested User Role:" + existUserRole.get().getRoleName() + " not Belongs to Company:"
								+ existGlobalCompany.get().getCompanyName());

			}

			// Check-ApplicationMenu-is-exist-and-active?
			Optional<ApplicationMenus> menuDetails = existMenuDetails.stream()
					.filter(menu -> menu.getApplicationMenuId() == singleRecord.getApplicationMenus().getApplicationMenuId()).findFirst();

			if (menuDetails.isEmpty()) {
				if (logger.isDebugEnabled())
					logger.debug("Requested menu not found! Searched by Id:"
							+ singleRecord.getApplicationMenus().getApplicationMenuId());

				throw new NoContentFoundException(SuccessCode.NO_CONTENT,
						"Requested menu not found! Searched by Id:" + singleRecord.getApplicationMenus().getApplicationMenuId());
			} else {

				// Check-is-it-Active?
				if (menuDetails.get().getIsActive() == false) {
					if (logger.isDebugEnabled())
						logger.debug("Requested Menu:" + menuDetails.get().getApplicationMenuId() + " is InActive");

					throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE, "Requested Menu is InActive");

				}

			}

			if (logger.isDebugEnabled())
				logger.debug("Checking Record information is already exist?");

			// Check-Already-Similar-Record-Already-exist?
			Optional<RolePrivilegesMatrix> alreadyExistRolePrivileges = allExistRolePrivileges.stream()
					.filter(role -> role.getUserRoles().equals(existUserRole.get())
							&& role.getGlobalCompanyMaster().equals(existGlobalCompany.get())
							&& role.getApplicationMenus().equals(menuDetails.get()))
					.findFirst();

			if (alreadyExistRolePrivileges.isPresent()) {
				if (logger.isDebugEnabled())
					logger.debug("Record with requested Privileges:" + singleRecord.toString()
							+ " already exist! no need to Create");

			} else {
				if (logger.isDebugEnabled())
					logger.debug("Record information is not exist Ready to Create");

				singleRecord.setCreatedBy(requestUtils.getUserName());
				singleRecord.setLastModifiedBy(requestUtils.getUserName());
				singleRecord.setIsActive(1);

				uniqueRecords.add(singleRecord);

			}

		} // Validation-for-close

		// Check-unique-list-is-empty?
		if (uniqueRecords.isEmpty()) {

			if (logger.isDebugEnabled())
				logger.debug("Requested Privileges are already exist");

			throw new ResourceAlreadyExistException(ErrorCode.ALREADY_EXIST, "Privileges already exist");
		}

		List<RolePrivilegesMatrix> savedPrivilegesList = new ArrayList<RolePrivilegesMatrix>();

		try {
			savedPrivilegesList = rolePrivilegesMatrixRepository.saveAll(uniqueRecords);
		} catch (Exception e) {

			if (logger.isDebugEnabled())
				logger.error("Failed to save Role Privileges details. Exception:" + e.getMessage());

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Failed to save Role Privileges details.");
		}

		if (logger.isDebugEnabled())
			logger.error(savedPrivilegesList.size() + ":Role Privileges Saved Successfully.");

		return savedPrivilegesList;
	}// Add-Close

	@Override
	public List<RolePrivilegesMatrix> updatePrivileges(List<RolePrivilegesMatrix> requestList) {

		if (logger.isDebugEnabled())
			logger.debug("In updatePrivileges Method from RolePrivilegesMatrix Service");

		/*
		 * Perform-basic-validations-for-all-records
		 */

		// Hold-All-exist-menus-to-search.
		List<ApplicationMenus> existMenuDetails = applicationMenusRepository.findAll();

		// Hold-All-Role-privileges-details-to-check-similar-record-request-is-there?
		List<RolePrivilegesMatrix> allExistRolePrivileges = rolePrivilegesMatrixRepository.findAll();

		// Updation-Requested-record-list
		List<RolePrivilegesMatrix> updationReqRecords = new ArrayList<RolePrivilegesMatrix>();

		for (RolePrivilegesMatrix singleRecord : requestList) {

			// Check-Requested-record-exist?-so-that-we-can-update-it?
			Optional<RolePrivilegesMatrix> existPrivileges = allExistRolePrivileges.stream()
					.filter(record -> record.getRolePrivilegeMatrixId() == singleRecord.getRolePrivilegeMatrixId())
					.findFirst();

			if (existPrivileges.isPresent()) {

				if (logger.isDebugEnabled())
					logger.debug("Record found by Id:" + singleRecord.getRolePrivilegeMatrixId() + ". Details:"
							+ existPrivileges.toString());

				// Check-Request-is-for-DeActivation?
				if (singleRecord.getIsActive() == 0 && existPrivileges.get().getIsActive() == 1) {

					if (logger.isDebugEnabled())
						logger.debug("Request is for De-Activation");

					existPrivileges.get().setLastModifiedBy(requestUtils.getUserName());
					existPrivileges.get().setIsActive(0);
					updationReqRecords.add(existPrivileges.get());

					continue;
				} else {
					// Check-Requested-UserRole-is-exist-and-Active?
					Optional<UserRole> existUserRole = userRoleRepository
							.findById(singleRecord.getUserRoles().getUserRoleId());

					if (existUserRole.isEmpty()) {
						if (logger.isDebugEnabled())
							logger.debug("Requested User Role not found! Searched by Id:"
									+ singleRecord.getUserRoles().getUserRoleId());

						throw new NoContentFoundException(SuccessCode.NO_CONTENT,
								"Requested User Role not found! Searched by Id:"
										+ singleRecord.getUserRoles().getUserRoleId());
					} else {

						// Check-is-it-Active?
						if (existUserRole.get().isActive() == false) {
							if (logger.isDebugEnabled())
								logger.debug("Requested User Role:" + singleRecord.getUserRoles() + " is InActive");

							throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE, "Requested User Role:"
									+ singleRecord.getUserRoles().getRoleName() + " is InActive");

						}
					}

					// check-Requested-globalComapny-is-exist-and-active?
					Optional<GlobalCompanyMaster> existGlobalCompany = companyMasterRepository
							.findById(singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

					if (existGlobalCompany.isEmpty()) {
						if (logger.isDebugEnabled())
							logger.debug("Requested Global Company not found! Searched by Id:"
									+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());

						throw new NoContentFoundException(SuccessCode.NO_CONTENT,
								"Requested Global Company not found! Searched by Id:"
										+ singleRecord.getGlobalCompanyMaster().getGlobalCompanyMasterId());
					} else {

						// Check-is-it-Active?
						if (existGlobalCompany.get().isActive() == false) {
							if (logger.isDebugEnabled())
								logger.debug("Requested Global Company:" + existGlobalCompany.get().getCompanyName()
										+ " is InActive");

							throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
									"Requested Company:" + existGlobalCompany.get().getCompanyName() + " is InActive");

						}

					}

					// Check-UserGroup-of-Requested-UserRole-does-belongs-to-the-requested-globalCompany?

					String compnayGroupCodeFromGlobalCompany = existGlobalCompany.get().getCompanyGroupCode().trim()
							.toUpperCase();

					/*
					 * This-CompanyGroupCode-is-nothing-but-the-companyGroupCode-of-
					 * CompanyGroupMaster
					 */
					if (existUserRole.get().getUserGroup().getCompanyGroupMaster().getCompanyGroupCode().trim()
							.toUpperCase().equalsIgnoreCase(compnayGroupCodeFromGlobalCompany)) {
						if (logger.isDebugEnabled())
							logger.debug("Requested User Role Belongs to Company:"
									+ existGlobalCompany.get().getCompanyName());

					} else {
						if (logger.isDebugEnabled())
							logger.debug("Requested User Role:" + existUserRole + " not Belongs to Company:"
									+ existGlobalCompany.get().getCompanyName());

						throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE,
								"Requested User Role:" + existUserRole.get().getRoleName() + " not Belongs to Company:"
										+ existGlobalCompany.get().getCompanyName());

					}

					// Check-ApplicationMenu-is-exist-and-active?
					Optional<ApplicationMenus> menuDetails = existMenuDetails.stream()
							.filter(menu -> menu.getApplicationMenuId() == singleRecord.getApplicationMenus().getApplicationMenuId())
							.findFirst();

					if (menuDetails.isEmpty()) {
						if (logger.isDebugEnabled())
							logger.debug("Requested menu not found! Searched by Id:"
									+ singleRecord.getApplicationMenus().getApplicationMenuId());

						throw new NoContentFoundException(SuccessCode.NO_CONTENT,
								"Requested menu not found! Searched by Id:"
										+ singleRecord.getApplicationMenus().getApplicationMenuId());
					} else {

						// Check-is-it-Active?
						if (menuDetails.get().getIsActive() == false) {
							if (logger.isDebugEnabled())
								logger.debug("Requested Menu:" + menuDetails.get().getApplicationMenuId() + " is InActive");

							throw new RequestNotAcceptable(ErrorCode.NOT_ACCEPTABLE, "Requested Menu is InActive");

						}

					}

					// Check-requested-information-is-already-used-by-another-record?
					if (logger.isDebugEnabled())
						logger.debug("Checking Record information is alredy exist?");

					// Check-Already-Similar-Record-Already-exist?
					Optional<RolePrivilegesMatrix> alreadyExistRolePrivileges = allExistRolePrivileges.stream()
							.filter(rolePriviliege -> rolePriviliege.getRolePrivilegeMatrixId() != singleRecord
									.getRolePrivilegeMatrixId()
									&& rolePriviliege.getUserRoles().equals(existUserRole.get())
									&& rolePriviliege.getGlobalCompanyMaster().equals(existGlobalCompany.get())
									&& rolePriviliege.getApplicationMenus().equals(menuDetails.get()))
							.findFirst();

					if (alreadyExistRolePrivileges.isPresent()) {
						if (logger.isDebugEnabled())
							logger.debug("Record with requested Privileges:" + singleRecord.toString()
									+ " is already used by another record.");

					} else {
						if (logger.isDebugEnabled())
							logger.debug(
									"Requested Record information is not exist in another record. Ready to Update");

						singleRecord.setCreatedBy(existPrivileges.get().getCreatedBy());
						singleRecord.setCreatedDateTime(existPrivileges.get().getCreatedDateTime());
						singleRecord.setLastModifiedBy(requestUtils.getUserName());
						singleRecord.setIsActive(1);

						updationReqRecords.add(singleRecord);

					}

				} // else-close-of-checking-DeActivation

			} else {
				if (logger.isDebugEnabled())
					logger.debug("Record with Id:" + singleRecord.getRolePrivilegeMatrixId() + " not exist");
			}

		} // Basic-validation-close

		// Check-updationReqRecords-list-is-empty?
		if (updationReqRecords.isEmpty()) {

			if (logger.isDebugEnabled())
				logger.debug("Requested Records are not exist!. Can not proceed");

			throw new NoContentFoundException(SuccessCode.NO_CONTENT,
					"Requested Records are not exist!. Can not proceed");
		}

		// Save-Updated-Details
		List<RolePrivilegesMatrix> savedPrivilegesList = new ArrayList<RolePrivilegesMatrix>();

		try {
			savedPrivilegesList = rolePrivilegesMatrixRepository.saveAll(updationReqRecords);
		} catch (Exception e) {

			if (logger.isDebugEnabled())
				logger.error("Failed to save Role Privileges details. Exception:" + e.getMessage());

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Failed to save Role Privileges details.");
		}

		if (logger.isDebugEnabled())
			logger.error(savedPrivilegesList.size() + ":Role Privileges Saved Successfully.");

		return savedPrivilegesList;

	}// update-Close

	/*
	 * To-Find-out-menu-has-parents-Menu?-if-Yes-Collect-his-hierarchy-and-return
	 * 
	 * private List<ApplicationMenuHierarchyBean>
	 * getParentMenus(ApplicationMenuDetails child, List<ApplicationMenuDetails>
	 * allExistMenus) {
	 * 
	 * List<ApplicationMenuHierarchyBean> childBeanslist = new ArrayList<>();
	 * 
	 * ApplicationMenuHierarchyBean childBeans = new ApplicationMenuHierarchyBean();
	 * 
	 * childBeans.setMenuId(child.getMenuId());
	 * childBeans.setParentMenuId(child.getParentMenuId());
	 * childBeans.setMenuDisplayName(child.getMenuDisplayName());
	 * childBeans.setDiscription(child.getDiscription());
	 * childBeans.setIsActive(child.getIsActive());
	 * childBeans.setCreatedBy(child.getCreatedBy());
	 * childBeans.setCreatedDateTime(child.getCreatedDateTime());
	 * 
	 * Optional<ApplicationMenuDetails> parentofChild = allExistMenus.stream()
	 * .filter(find -> find.getMenuId() == child.getParentMenuId()).findFirst();
	 * 
	 * List<ApplicationMenuHierarchyBean> childAsParentBean = new
	 * ArrayList<ApplicationMenuHierarchyBean>();
	 * 
	 * if (!parentofChild.isEmpty()) { if (logger.isDebugEnabled())
	 * logger.debug("Child has parent: "+ parentofChild.toString());
	 * 
	 * childAsParentBean = getParentMenus(parentofChild.get(), allExistMenus);
	 * 
	 * childBeans.setParentChildRelation(childAsParentBean); } else { if
	 * (logger.isDebugEnabled()) logger.debug("Does not have Parent");
	 * childBeans.setParentChildRelation(null); }
	 * 
	 * childBeanslist.add(childBeans); return childBeanslist;
	 * }//getParentMenus-Close
	 */
}
