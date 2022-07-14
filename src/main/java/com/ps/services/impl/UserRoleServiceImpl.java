package com.ps.services.impl;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ps.RESTful.dto.mapper.UserRoleDTOMapper;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.dto.ErrorDTO;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserRole;
import com.ps.entities.master.UserRoleHistory;
import com.ps.services.UserRoleService;
import com.ps.services.dao.repository.master.UserGroupRepository;
import com.ps.services.dao.repository.master.UserRoleHistoryRepository;
import com.ps.services.dao.repository.master.UserRoleRepository;
import com.ps.util.MethodValidationUtils;
import com.ps.util.RequestUtils;
import com.ps.util.StringUtils;

@Service
public class UserRoleServiceImpl implements UserRoleService{

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	UserRoleHistoryRepository userRoleHistoryRepository;
	
	@Autowired
	UserGroupRepository userGroupRepository;
	
	@Autowired
	UserRoleDTOMapper dTOMapper;
	
	@Autowired
	RequestUtils requestUtils;
	
	// get all UserGroups
	@Override
	public List<UserRole> getAll(int companyGroupMasterId) {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllUserRoles method");
		List<UserRole> list = userRoleRepository.findAllActiveByCompanyGroupId(companyGroupMasterId);
		return list;
	}
	
	 @Override 
	 @Transactional("masterTransactionManager")
	  public UserRole add(UserRole userRoleDetails, int companyGroupMasterId) 
	  { 		  
		   if (logger.isDebugEnabled()) {
		   logger.debug("In Add User Role Details service method:  " + userRoleDetails);}  
		 
		   //Validating User Role data
		   validate(userRoleDetails, "add", companyGroupMasterId); 
				  
		  try 
		  {			  
			// SavingDetails_In_DB	
			  userRoleDetails.setCreatedBy(requestUtils.getUserName());			  		  
			  userRoleDetails = userRoleRepository.save(userRoleDetails); 
		  } 
		  catch (Exception e) 
		  {
			  if (logger.isDebugEnabled()) {
				  logger.debug("Failed to save User Role Details: " + userRoleDetails + ": getLocalizedMessage: "
							+ e.getLocalizedMessage() + "\n getMessage:" + e.getMessage()); 
			  throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to save User Role Details"); }
		  }
		  
		  if (logger.isDebugEnabled()) {
			  logger.debug("Company Registration Details saved successfully:" + userRoleDetails); 
			  }
		  
		  	return userRoleDetails;
	  }
	 
	 @Override
	  @Transactional("masterTransactionManager")
	  public UserRole update(UserRole userRoleDetails, int companyGroupMasterId) 
	  {
		 if (logger.isDebugEnabled()) {
		   logger.debug("In update User Role Details service method:  " + userRoleDetails);}		 			
			
			// Check_Id_is_Null
			MethodValidationUtils.checkIfIdIsZero(userRoleDetails.getUserRoleId(), "UserRoleId");
			
			// Fetch_Existing_Details
			Optional<UserRole> optionalDB = userRoleRepository.findByUserRoleId(userRoleDetails.getUserRoleId());
			
			if (!optionalDB.isPresent()) {
				logger.error("User Role Details not found for Id: " + userRoleDetails.getUserRoleId());
				throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User Role Details not found for Id: " + userRoleDetails.getUserRoleId());
			}
			
			// Validating User Role data
			validate(userRoleDetails, "update", companyGroupMasterId);
								
			//assigning history data 
			boolean isDataModified = false;
			UserRole userRoleDB =  optionalDB.get();
			UserRoleHistory history = new UserRoleHistory();
			isDataModified = isDataModified(userRoleDB, userRoleDetails);
			if (logger.isDebugEnabled()) {
				   logger.debug("User Role Details present in DB: " + userRoleDB);
				   logger.debug("User Role Details new data request: " + userRoleDetails);	   
			}
			
			if(isDataModified) {						
				history = dTOMapper.entityToEntityHistory(userRoleDB, history);
			}	

			// Set_Existing_Details_to_New_Request
			optionalDB.get().setLastModifiedBy(requestUtils.getUserName());

			// ConvertingEntity
			optionalDB = dTOMapper.entityToEntity(userRoleDetails, optionalDB);			

			UserRole userRoleResult = null;
				try {
					// SavingDetails_In_DB					
					userRoleResult = userRoleRepository.save(optionalDB.get());
					
					//save history data
					if (isDataModified) {
					saveHistoryData(history);
					}
																																
				} 
				catch (Exception e) {
				if (logger.isDebugEnabled())
					logger.debug("Failed to update User Role Details: " + userRoleDetails + ": getLocalizedMessage: "
							+ e.getLocalizedMessage() + "\n getMessage:" + e.getMessage());

				throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed To Update User Role Details");
			}

			if (logger.isDebugEnabled())
				logger.debug("In update service User Role Details Service Method, record updated for UserRoleID: "
						+ userRoleDetails.getUserRoleId() + " with Details:" + userRoleResult);

			return userRoleResult;								
			
		}
	 
	 private Void validate(UserRole userRoleData, String addUpdate, int companyGroupMasterId) {
			//validating UserRole name
			if (logger.isDebugEnabled())
				logger.debug("In UserRole service validate method");
										
			//company Registration ID should be 0 
			String error = "User Role Id is Invalid -> " + userRoleData.getUserRoleId();
			if (addUpdate == "add" &&  userRoleData.getUserRoleId() != 0) {
				if (logger.isDebugEnabled())
					logger.debug(error);
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, error); 
			}
			
			//company Registration ID mandatory
			if (addUpdate == "update" &&  userRoleData.getUserRoleId() == 0) {
				if (logger.isDebugEnabled())
					logger.debug(error);
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, error); 
			}
			
			//validating User Group ID
			if (!CheckUserGroupIdExistOrNot(userRoleData.getUserGroup().getUserGroupId(), companyGroupMasterId)) {
				if (logger.isDebugEnabled())
					logger.debug("User Group Id not exist");
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User Group Id not exist"); 
			}	
			
			//validating RoleName mandatory
			if (!StringUtils.isValidString(userRoleData.getRoleName())) {
				if (logger.isDebugEnabled())
					logger.debug("User Role Name is mandatory -> " + userRoleData.getRoleName());
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User Role Name is mandatory -> " + userRoleData.getRoleName()); 
		    }
			
			// validating Role Name to avoid duplicate Role Name
			if (StringUtils.isValidString(userRoleData.getRoleName())) {
				if (logger.isDebugEnabled())
					logger.debug("Role Name is -> " + userRoleData.getRoleName());
				Optional<UserRole> optional = userRoleRepository.findByUserRoleNameAndUserGroupId(userRoleData.getRoleName(), userRoleData.getUserGroup().getUserGroupId());
				if (addUpdate == "add" && optional.isPresent()) {
					if (logger.isDebugEnabled()) {
						logger.debug("Role Name is Already Exist: " + userRoleData.getRoleName());
					}
					throw new InvalidRequestException(ErrorCode.ALREADY_EXIST, "Role Name is Already Exist: " + userRoleData.getRoleName());					
				} else if (addUpdate == "update") {
					if (optional.isPresent()
							&& optional.get().getUserRoleId() != userRoleData.getUserRoleId()) {
						if (logger.isDebugEnabled()) {
							logger.debug("Role Name is Already Exist: " + userRoleData.getRoleName());
						}
						throw new InvalidRequestException(ErrorCode.ALREADY_EXIST, "Role Name is Already Exist: " + userRoleData.getRoleName());	
					}
				}
			}
			
			//validating Role Description mandatory
			if (!StringUtils.isValidString(userRoleData.getRoleDescription())) {
				if (logger.isDebugEnabled())
					logger.debug("User Role Description is mandatory -> " + userRoleData.getRoleDescription());
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User Role Description is mandatory -> " + userRoleData.getRoleDescription()); 
		    }
			
			//User Group ID mandatory
			if (userRoleData.getUserGroup().getUserGroupId() == 0) {
				if (logger.isDebugEnabled())
					logger.debug("User Group Id is Invalid -> " + userRoleData.getUserGroup().getUserGroupId());
				throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User Group Id is Invalid!"); 
			}	

			// Remark validation
			if (!userRoleData.isActive()) {
				if (!StringUtils.isValidString(userRoleData.getRemark())) {
					if (logger.isDebugEnabled())
						logger.debug("Remark is mandatory while De-Activating the User Role");
					throw new InvalidRequestException(ErrorCode.BAD_REQUEST,
							"Remark is mandatory while De-Activating the User Role!");

				}
			}
																											
			return null;
		}
	 
		// validating for Check userGroupId present or not in DB
		private boolean CheckUserGroupIdExistOrNot(int userGroupId, int companyGroupMasterId) {
			if (logger.isDebugEnabled())
				logger.debug("In CheckUserGroupIdExistOrNot method: User Group Id: " + userGroupId +  "Company Group ID :" +companyGroupMasterId);

			boolean CheckUserGroupIdExistOrNot = false;

			Optional<UserGroup> optionaDB = userGroupRepository
					.findByUserGroupAndCompanyGroupId(userGroupId, companyGroupMasterId);
			if (optionaDB.isPresent()) {
				CheckUserGroupIdExistOrNot = true;
				if (logger.isDebugEnabled())
					logger.debug("User Group Id value is valid:" + userGroupId);
			}
			return CheckUserGroupIdExistOrNot;
		}
		
		private boolean isDataModified(UserRole userRoleDB, UserRole userRoleReq) {
			boolean isDataModified = false;	
			  
			  if (!MethodValidationUtils.isStringsEqual(userRoleReq.getRoleName(), userRoleDB.getRoleName()))  {
				  isDataModified = true;
					 if (logger.isDebugEnabled()) {
						 logger.debug("Change detected in RoleName:");
						 logger.debug("Change detected RoleNameDB:"+ userRoleDB.getRoleName());
						 logger.debug("Change detected in RoleNameReq:"+ userRoleReq.getRoleName());				  
					  }
					 return isDataModified;	
			  }
			  
			  if (!MethodValidationUtils.isStringsEqual(userRoleReq.getRemark(), userRoleDB.getRemark()))  {
				  isDataModified = true;
					 if (logger.isDebugEnabled()) {
						 logger.debug("Change detected in Remark:");
						 logger.debug("Change detected RemarkDB:"+ userRoleDB.getRemark());
						 logger.debug("Change detected in RemarkReq:"+ userRoleReq.getRemark());				  
					  }
					 return isDataModified;	
			  }
			  
			  if (!MethodValidationUtils.isStringsEqual(userRoleReq.getRoleDescription(), userRoleDB.getRoleDescription()))  {
				  isDataModified = true;
					 if (logger.isDebugEnabled()) {
						 logger.debug("Change detected in RoleDescription:");
						 logger.debug("Change detected RoleDescriptionDB:"+ userRoleDB.getRoleDescription());
						 logger.debug("Change detected in RoleDescriptionReq:"+ userRoleReq.getRoleDescription());				  
					  }
					 return isDataModified;	
			  }		 
			  
			  boolean isActiveDB = userRoleDB.isActive();
			  boolean isActiveReq = userRoleReq.isActive();
			  if (Boolean.compare(isActiveDB, isActiveReq) != 0)
			  {
				  if (logger.isDebugEnabled()) {
					  logger.debug("Change detected in isActive:");
						 logger.debug("Change detected in isActiveDB:"+ isActiveDB);	
						 logger.debug("Change detected in isActiveReq:"+ isActiveReq);	
					  }
				  isDataModified = true;
				  return isDataModified;
			  }
			  
			  boolean isDefaulteDB = userRoleDB.isDefault();
			  boolean isDefaultReq = userRoleReq.isDefault();
			  if (Boolean.compare(isDefaulteDB, isDefaultReq) != 0)
			  {
				  if (logger.isDebugEnabled()) {
					  logger.debug("Change detected in isDefault:");
						 logger.debug("Change detected in isDefaultDB:"+ isDefaulteDB);	
						 logger.debug("Change detected in isDefaultReq:"+ isDefaultReq);	
					  }
				  isDataModified = true;
				  return isDataModified;
			  }	
			return isDataModified;
		}
		
		private void saveHistoryData(UserRoleHistory history) {
	    	   
	    	   if (logger.isDebugEnabled()) {
				   logger.debug("Saving User Role Details History:"+ history);
																			
				MethodValidationUtils.checkIfDatabaseObjectIsNotNULL(history, UserRoleHistory.class.getSimpleName());}
				
	    	   userRoleHistoryRepository.save(history);
	       }
}
