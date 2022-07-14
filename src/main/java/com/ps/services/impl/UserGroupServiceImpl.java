package com.ps.services.impl;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ps.RESTful.dto.mapper.UserGroupDTOMapper;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.beans.UserGroupBean;
import com.ps.beans.UserGroupRequestBean;
import com.ps.dto.ErrorDTO;
import com.ps.entities.master.CompanyGroupMaster;
import com.ps.entities.master.CompanyGroupMasterBean;
import com.ps.entities.master.UserGroup;
import com.ps.entities.master.UserGroupHistory;
import com.ps.entities.master.UserGroupReport;
import com.ps.services.UserGroupService;
import com.ps.services.dao.repository.master.CompanyGroupMasterBeanRepository;
import com.ps.services.dao.repository.master.CompanyGroupMasterRepository;
import com.ps.services.dao.repository.master.UserGroupHistoryRepository;
import com.ps.services.dao.repository.master.UserGroupReportRepository;
import com.ps.services.dao.repository.master.UserGroupRepository;
import com.ps.services.dao.repository.master.UserRoleRepository;
import com.ps.util.MethodValidationUtils;
import com.ps.util.RequestUtils;
import com.ps.util.StringUtils;


@Service
public class UserGroupServiceImpl implements UserGroupService {

	Logger logger = Logger.getLogger(getClass());

	@Autowired
	UserGroupRepository userGroupRepository;
	
	@Autowired
	CompanyGroupMasterBeanRepository companyGroupMasterBeanRepository;
	
	@Autowired
	UserGroupHistoryRepository userGroupHistoryRepository;
	
	@Autowired
	UserGroupReportRepository userGroupReportRepository;
	
	@Autowired
	UserRoleRepository userRoleRepository;
	
	@Autowired
	CompanyGroupMasterRepository companyGroupMasterRepository;
	
	@Autowired
	RequestUtils requestUtils;
	
	@Autowired
	UserGroupDTOMapper dtoMapper;

	// get all UserGroups
	@Override
	public List<UserGroup> getAllUserGroupsByCompanyGroupIds(List<Integer> companyGroupMasterIds) {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllUserGroupsByCompanyGroupIds method");
		List<UserGroup> list = userGroupRepository.findAllActiveByCompanyGroupId(companyGroupMasterIds);
		return list;
	}
	
	// get all Distinct UserGroups
	@Override
	public List<UserGroupReport> getAllDistinctUserGroups() {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllDistinctUserGroups method");
		List<UserGroupReport> list = userGroupReportRepository.findAllDistinctUserGroups("", "", "", false,  "", false, "", "distinct");
		return list;
	}
	
	// get all UserGroups
	@Override
	public List<UserGroupBean> getAllUserGroupsByCompanyGroupId(Integer CompanyGroupId) {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllUserGroups method");
		List<UserGroupBean> list = userGroupRepository.findAllUserGroupsByCompanyGroupId(CompanyGroupId);
		return list;
	}

	// get all CompanyGroupByUserGroupName
	@Override
	public List<UserGroupBean> getAllCompanyGroupByUserGroupName(String groupName,
			List<Integer> companyGroupMasterIds) {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllCompanyGroupByUserGroupName method");
		List<UserGroupBean> list = userGroupRepository.findAllCompanyGroupNamesByUserGroupName(groupName,
				companyGroupMasterIds);
		return list;
	}
	
	// get all Distinct User Groups by CompanyGroupIds
	@Override
	public List<UserGroupBean> getAllDistinctUserGroups(List<Integer> listCompanyGroupIds) {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllDistinctUserGroups method");
		List<UserGroupBean> list = userGroupRepository.findDistinctUserGroups(listCompanyGroupIds);
		// Create a list with the distinct elements using stream.
//		List<UserGroupBean> listDistinct = list.stream().distinct().collect(Collectors.toList());
		 // Get distinct objects by key
        List<UserGroupBean> listDistinct = list.stream()
                    .filter( distinctByKey(p -> p.getGroupName()) )
                    .collect( Collectors.toList() );
		
		return listDistinct;
	}
	
	   //Utility function
    public static <T> Predicate<T> distinctByKey(Function<? super T, Object> keyExtractor) 
    {
        Map<Object, Boolean> map = new ConcurrentHashMap<>();
        return t -> map.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
    }
	
	// get all CompanyGroups  by UserGroups
	@Override
	public List<CompanyGroupMasterBean> getAllCompanyGroupByUserGroups(String groupName, List<Integer> listCompanyGroupIds) {
		if (logger.isDebugEnabled())
			logger.debug("In UserGroupService getAllCompanyGroupByUserGroups method");
//		List<Integer> Ids = new ArrayList<>();
		if (listCompanyGroupIds.isEmpty()) {
			listCompanyGroupIds.add(0);
		}
//		List<Object> list = userGroupRepository.findAllCompanyGroupsByUserGroup(groupName);
		List<CompanyGroupMasterBean> list = companyGroupMasterBeanRepository.findAllCompanyGroupsByUserGroupAndCompanyGroup(groupName, listCompanyGroupIds);
		return list;
	}

//	// get all UserGroups
//	@Override
//	public List<UserRole> getAllUserRole() {
//		if (logger.isDebugEnabled())
//			logger.debug("In UserGroupService getAllUserRoles method");
//		List<UserRole> list = userRoleRepository.findAll();
//		return list;
//	}
	
	 @Override
	  public UserGroupRequestBean getUserGroupDetails(String groupName) {

	    if (logger.isDebugEnabled())
	      logger.debug("In getUserGroupDetails service method for groupName: " + groupName);

	    UserGroupRequestBean  result =  new UserGroupRequestBean();
	    Optional<UserGroupReport> groupDetailsDb = userGroupReportRepository.GetUserGroupDetails("", groupName, "", false,  "", false, "", "report");

	    if (logger.isDebugEnabled()) {
	    	logger.debug("groupDetailsDb: " + groupDetailsDb);
	    }		      
	    
	    if (!groupDetailsDb.isPresent()) {
	      logger.error("Record not found for User Group: " + groupName);
	      throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User Group Details not found!");
	    }
	    
	    List<CompanyGroupMasterBean> groupMasterIdList = new ArrayList<>();
		CompanyGroupMasterBean groupIds ;
		List<String> companyGroupIdsList = new ArrayList<>();
		companyGroupIdsList = Arrays.asList(groupDetailsDb.get().getCompanyGroupIds().split(",")); 

		 if (logger.isDebugEnabled()) {
		    	logger.debug("companyGroupIdsList: " + companyGroupIdsList);
		    	logger.debug("companyGroupIdsList size: " + companyGroupIdsList.size());
		    }
		 
			for (int i = 0; i < companyGroupIdsList.size(); i++) {
				groupIds = new CompanyGroupMasterBean();
				groupIds.setCompanyGroupMasterId(Integer.parseInt(companyGroupIdsList.get(i)));
				groupMasterIdList.add(groupIds);
			}
	    
	    result.setGroupName(groupDetailsDb.get().getGroupName());
	    result.setGroupDescription(groupDetailsDb.get().getGroupDescription());
	    result.setRemark(groupDetailsDb.get().getRemark());
	    result.setCompanyGroupIds(groupMasterIdList);
	    result.setDefault(groupDetailsDb.get().isDefault());
	    result.setActive(groupDetailsDb.get().isActive());
	  
	    logger.debug("In getUserGroupDetails Service Method, records found for groupName: " + groupName);
	    return result;
	  }

	@Override
	@Transactional("masterTransactionManager")
	public List<UserGroup> addAllUserGroup(List<UserGroup> list) {
		if (logger.isDebugEnabled()) {
			logger.debug("In User Group addAllUserGroup method, saving User Group list: " + list);
		}
		// add/update data into GlobalCompanyMaster table
		return saveUserGroup(list);
	}	
	
	@Override
	@Transactional("masterTransactionManager")
	public String addAll(UserGroupRequestBean userGroupBean) {
		if (logger.isDebugEnabled()) {
			logger.debug("In User Group addAllUserGroup method, saving User Group list: " );
		}
		// add/update data into GlobalCompanyMaster table
		userGroupBean.setCreatedBy("Test User");
		return SaveUserGroupNew(userGroupBean);
	}	
	
	
	public String SaveUserGroupNew(UserGroupRequestBean userGroupBean) {		
		List<UserGroup> objResult = new ArrayList<>();
		String strCompanyGroupIds = "";
		String result = "";
		
//		validating data while adding new records
		ErrorDTO error = validateUserGroupFieldsNew(userGroupBean);
		MethodValidationUtils.errorValidation(error);
		
		strCompanyGroupIds = userGroupBean.getCompanyGroupIds()
		        .stream()
		        .map(a -> String.valueOf(a.getCompanyGroupMasterId()))
		        .collect(Collectors.joining(","));
					
		if (logger.isDebugEnabled())
			logger.debug("In Service, Calling SP");			
			
		result = userGroupReportRepository.SaveUserGroup(strCompanyGroupIds, userGroupBean.getGroupName(), userGroupBean.getGroupDescription(),
				userGroupBean.isDefault(), userGroupBean.getRemark(), userGroupBean.isActive(), requestUtils.getUserName(),"add");				
		
//		List<CompanyGroupMasterBean> resultList = new ArrayList<>();
//		CompanyGroupMasterBean groupIds ;
//		List<String> companyGroupIdsList = Arrays.asList(result.getCompanyGroupIds());			
//		 for (String id : companyGroupIdsList) {
//			 groupIds = new CompanyGroupMasterBean();
//			 groupIds.setCompanyGroupMasterId(Integer.valueOf(id));
//			 resultList.add(groupIds);
//	      }		 
		return result;
	}
	
	
	private List<UserGroup> saveUserGroup(List<UserGroup> userGroupList) {
						
		if (logger.isDebugEnabled())
			logger.debug("In saveUserGroup method of UserGroup service class");
		
		// check the list is empty or not
	    MethodValidationUtils.checkIfObjectListIsNotEmpty(userGroupList, UserGroup.class.getSimpleName());
	    
	    int counter = 0;	  	    
		List<UserGroup> userGroupDBList = new ArrayList<>();	
		List<UserGroupHistory> userGroupHistoryist = new ArrayList<>();
	
		//Check CheckCompanyNameRepeatOrNot		
		int duplicateCompanyGroupId = CheckCompanyGroupIdRepeatOrNot(userGroupList);
		if(duplicateCompanyGroupId != 0) {
			if (logger.isDebugEnabled())
				logger.debug("Same Company Group Id found multiple times:" + duplicateCompanyGroupId);
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Same Company Group Id found multiple times:" + duplicateCompanyGroupId);
		}		
				
		// iterate the list
		for (int i = 0; i < userGroupList.size(); i++) {
			UserGroup userGroupMaster = new UserGroup();
			userGroupMaster = userGroupList.get(i);

			if (userGroupMaster.getCompanyGroupMaster().getCompanyGroupMasterId() != 0 && userGroupMaster.getCompanyGroupMaster().getCompanyGroupMasterId() > 0) {
				if (!IsCompanyGroupPresent(userGroupMaster.getCompanyGroupMaster().getCompanyGroupMasterId(), companyGroupMasterRepository)) {
					if (logger.isDebugEnabled())
						logger.debug("Company Group Id not exist:" + userGroupMaster.getCompanyGroupMaster().getCompanyGroupMasterId());
					throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, "Company Group Id not exist:" + userGroupMaster.getCompanyGroupMaster().getCompanyGroupMasterId());
				}
			}			

			// if new record then add new entry or update the existing data
			if (userGroupMaster.getUserGroupId() == 0) {
				if (logger.isDebugEnabled())
					logger.debug("adding new User Group data into database");
				
				//validating data while adding new records
				ErrorDTO error = validateUserGroupFields(userGroupMaster, "add");
				MethodValidationUtils.errorValidation(error);
				
				userGroupMaster.setCreatedBy(requestUtils.getUserName());				
				userGroupDBList.add(userGroupMaster);
			}else {
				if (logger.isDebugEnabled())
					logger.debug("updating existing User Group data into database");
				
				Optional<UserGroup> optionalUserGroupDB = userGroupRepository.findByuserGroupId(userGroupMaster.getUserGroupId());
				
				if (!optionalUserGroupDB.isPresent()) {
					if (logger.isDebugEnabled())
						logger.debug("User Group data not found for Id:" + userGroupMaster.getUserGroupId());
					throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "User Group data not found for Id:" + userGroupMaster.getUserGroupId());
				}
				
				boolean isDataModified = false;
				
				UserGroup UserGroupDB =  optionalUserGroupDB.get();
				UserGroupHistory history = new UserGroupHistory();
				
				//validating data while adding new records
				ErrorDTO error = validateUserGroupFields(userGroupMaster, "update");
				MethodValidationUtils.errorValidation(error);
				
				
				isDataModified = isDataModified(UserGroupDB, userGroupMaster);
				
				if (logger.isDebugEnabled()) {
					   logger.debug("User Group Details present in DB: " + UserGroupDB);
					   logger.debug("User Group Details new data request: " + userGroupMaster);	   
				}
				
				if(isDataModified) {						
					history = dtoMapper.entityToEntityHistory(UserGroupDB, history);
					userGroupHistoryist.add(history);
				}
				
				optionalUserGroupDB = dtoMapper.entityToEntity(userGroupMaster, optionalUserGroupDB);					
				optionalUserGroupDB.get().setLastModifiedBy(requestUtils.getUserName());			

				userGroupDBList.add(optionalUserGroupDB.get());	
			}
		}		

		List<UserGroup> resultList = new ArrayList<>();
		
		try {										
				// check the list is empty or not
			    MethodValidationUtils.checkIfObjectListIsNotEmpty(userGroupDBList, UserGroup.class.getSimpleName());
			    
			  //saving data into DB
				if (logger.isDebugEnabled())
					logger.debug("Saving User Group list into DB");
				resultList = userGroupRepository.saveAll(userGroupDBList);	
				//saving history data											
				if (!userGroupHistoryist.isEmpty()) {
					userGroupHistoryRepository.saveAll(userGroupHistoryist);
				}
		}
		catch (Exception e) {
		      if (logger.isDebugEnabled())
		        logger.debug("Failed to Save User Group Master details: " + userGroupDBList + ": getLocalizedMessage: "
		            + e.getLocalizedMessage() + "\n getMessage:" + e.getMessage());

		      throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed To Save User Group Master details");
		    }
		
		// check the list is empty or not
	    MethodValidationUtils.checkIfObjectListIsNotEmpty(resultList, UserGroup.class.getSimpleName());
		if (logger.isDebugEnabled())
			logger.debug("User Group Master details added/updated into database successfully: " + userGroupDBList);

		return resultList;
	}
	
	private boolean isDataModified(UserGroup userGroupDB, UserGroup userGroupReq) {
		boolean isDataModified = false;	
		  
		  if (!MethodValidationUtils.isStringsEqual(userGroupReq.getGroupName(), userGroupDB.getGroupName()))  {
			  isDataModified = true;
				 if (logger.isDebugEnabled()) {
					 logger.debug("Change detected in GroupName:");
					 logger.debug("Change detected GroupNameDB:"+ userGroupDB.getGroupName());
					 logger.debug("Change detected in GroupNameReq:"+ userGroupReq.getGroupName());				  
				  }
				 return isDataModified;	
		  }
		  
		  if (!MethodValidationUtils.isStringsEqual(userGroupReq.getRemark(), userGroupDB.getRemark()))  {
			  isDataModified = true;
				 if (logger.isDebugEnabled()) {
					 logger.debug("Change detected in Remark:");
					 logger.debug("Change detected RemarkDB:"+ userGroupDB.getRemark());
					 logger.debug("Change detected in RemarkReq:"+ userGroupReq.getRemark());				  
				  }
				 return isDataModified;	
		  }
		  
		  if (!MethodValidationUtils.isStringsEqual(userGroupReq.getGroupDescription(), userGroupDB.getGroupDescription()))  {
			  isDataModified = true;
				 if (logger.isDebugEnabled()) {
					 logger.debug("Change detected in GroupDescription:");
					 logger.debug("Change detected GroupDescriptionDB:"+ userGroupDB.getGroupDescription());
					 logger.debug("Change detected in GroupDescriptionReq:"+ userGroupReq.getGroupDescription());				  
				  }
				 return isDataModified;	
		  }		 
		  
		  boolean isActiveDB = userGroupDB.isActive();
		  boolean isActiveReq = userGroupReq.isActive();
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
		  
		  boolean isDefaulteDB = userGroupDB.isDefault();
		  boolean isDefaultReq = userGroupReq.isDefault();
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
	
	// CheckCompanyGroupIdRepeatOrNot in request
	private int CheckCompanyGroupIdRepeatOrNot(List<UserGroup> list) {
		if (logger.isDebugEnabled())
			logger.debug("In CheckCompanyGroupIdRepeatOrNot method");

		int duplicateGroupId = 0 ;
		Set<Integer> set = new HashSet<Integer>();
		// iterate the list
		for (int i = 0; i < list.size(); i++) {
			UserGroup groupMaster = list.get(i);
			if (groupMaster.getCompanyGroupMaster().getCompanyGroupMasterId() != 0) {
				int groupId = groupMaster.getCompanyGroupMaster().getCompanyGroupMasterId();
				if (set.contains(groupId)) {
					return groupId;
				}			
				set.add(groupId);				
			}
		}
		return duplicateGroupId;
	}
	
	// CheckCompanyGroupId exists OrNot
	private boolean IsCompanyGroupPresent(int companyGroupId, CompanyGroupMasterRepository repository) {
		boolean result = false;
		if (logger.isDebugEnabled())
			logger.debug("In checkIfCompanyGroupPresent method checking company group is present or not.");
		Optional<CompanyGroupMaster> groupMasterDB = repository.findActiveCompanyGroup(companyGroupId);
		if (groupMasterDB.isPresent()) {
			result = true;
		}
		return result;
	}
	
	// validating User Group Data fields
	private ErrorDTO validateUserGroupFields(UserGroup userGroup, String addOrUpdate) {
		// validating company Name to avoid duplicate company Name
		if (StringUtils.isValidString(userGroup.getGroupName())
				&& userGroup.getCompanyGroupMaster().getCompanyGroupMasterId() != 0) {
			if (logger.isDebugEnabled())
				logger.debug("User Group Name is -> " + userGroup.getGroupName());
			Optional<UserGroup> optional = userGroupRepository.findByGroupNameAndCompanyGroupId(
					userGroup.getGroupName(), userGroup.getCompanyGroupMaster().getCompanyGroupMasterId());
			if (addOrUpdate == "add" && optional.isPresent()) {
				if (logger.isDebugEnabled()) {
					logger.debug(
							"User Group Name is Already Exist: " + userGroup.getGroupName() + " for company group--> "
									+ optional.get().getCompanyGroupMaster().getCompanyGroupMasterId());
				}

				return new ErrorDTO(ErrorCode.ALREADY_EXIST,
						"User Group Name is Already Exist: " + userGroup.getGroupName() + " for company group--> "
								+ optional.get().getCompanyGroupMaster().getCompanyGroupMasterId());
			} else if (addOrUpdate == "update") {
				if (optional.isPresent() && optional.get().getUserGroupId() != userGroup.getUserGroupId()) {

					if (logger.isDebugEnabled()) {
						logger.debug("User Group Name is Already Exist: " + userGroup.getGroupName()
								+ " for company group--> "
								+ optional.get().getCompanyGroupMaster().getCompanyGroupMasterId());
					}

					return new ErrorDTO(ErrorCode.ALREADY_EXIST,
							"User Group Name is Already Exist: " + userGroup.getGroupName() + " for company group--> "
									+ optional.get().getCompanyGroupMaster().getCompanyGroupMasterId());
				}
			}
		}
		return null;
	}
	
	// validating User Group Data fields
		private ErrorDTO validateUserGroupFieldsNew(UserGroupRequestBean userGroupBean) {
			// validating GroupName
			if (!StringUtils.isValidString(userGroupBean.getGroupName())) {
				if (logger.isDebugEnabled())
					logger.debug("User Group Name not provided ");
				return new ErrorDTO(ErrorCode.INVALID_PARAMETER, "User Group Name not provided ");
			}

			// validating GroupDescription
			if (!StringUtils.isValidString(userGroupBean.getGroupDescription())) {
				if (logger.isDebugEnabled())
					logger.debug("User Group Description not provided ");
				return new ErrorDTO(ErrorCode.INVALID_PARAMETER, "User Group Description not provided ");
			}

			// Remark validation
			if (!userGroupBean.isActive()) {
				if (!StringUtils.isValidString(userGroupBean.getRemark())) {
					if (logger.isDebugEnabled())
						logger.debug("Remark is mandatory while De-Activating the User Group");
					return new ErrorDTO(ErrorCode.INVALID_PARAMETER,
							"Remark is mandatory while De-Activating the User Group");
				}
			}
			return null;
		}
}
