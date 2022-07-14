package com.ps.services.impl;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.ResourceNotFoundException;
import com.ps.entities.master.GroupDBMaster;
import com.ps.services.GroupDBMasterService;
import com.ps.services.dao.repository.master.GroupDBMasterRepository;

@Service
public class GroupDBMasterServiceImpl implements GroupDBMasterService {

	Logger logger = Logger.getLogger(GroupDBMasterServiceImpl.class);
	
	@Autowired
	GroupDBMasterRepository groupDBMasterRepository;
	
	@Override
	public List<GroupDBMaster> getAll() {
		
		if(logger.isDebugEnabled()) logger.debug("In getAllGroupDBMasterData "
				+ "service method retrieving all group companies from databse");	
		return groupDBMasterRepository.findAll();
		
	}

	@Override
	public GroupDBMaster getById(int id) {
		
		if(logger.isDebugEnabled()) logger.debug("In getById "
				+ "service method retrieving group company from databse for company id-> "+id);
		
		Optional<GroupDBMaster> groupCompany = groupDBMasterRepository.findById(id);
		
		if(!groupCompany.isPresent()) {			
			if(logger.isDebugEnabled()) logger.debug("Group company not found in databse for group id-> "+id);			
			throw new ResourceNotFoundException(ErrorCode.RESOURCE_NOT_FOUND, "Group company not found!"); 
		}
		
		return groupCompany.get();
	}

}
