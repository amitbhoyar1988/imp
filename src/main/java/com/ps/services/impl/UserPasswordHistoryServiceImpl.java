package com.ps.services.impl;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.entities.master.UserPasswordHistory;
import com.ps.services.UserPasswordHistoryService;
import com.ps.services.dao.repository.master.UserPasswordHistoryRepository;

@Service
public class UserPasswordHistoryServiceImpl implements UserPasswordHistoryService{

	Logger logger = Logger.getLogger(UserPasswordHistoryServiceImpl.class);

	@Autowired
	UserPasswordHistoryRepository passwordHistoryRepository;
	
	@Override
	public void add(UserPasswordHistory passwordHistory) {

		validate(passwordHistory);
		passwordHistoryRepository.save(passwordHistory);
	}

	@Override
	public List<UserPasswordHistory> getAllByUserId(int userId) {
		
		if(logger.isDebugEnabled()) logger.debug("In password history getById service method, finding history for user with  id-> "+userId);
		if(userId <= 0) 
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User Id is invalid");
		
		List<UserPasswordHistory> historyList = passwordHistoryRepository.findAllByUserId(userId);
		
		return historyList;
	}
	
	private void validate(UserPasswordHistory passwordHistory) {
	
		if(StringUtils.isBlank(passwordHistory.getPassword())) {
			if(logger.isDebugEnabled()) logger.debug("Password is blank "+passwordHistory.getPassword());
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Password is not set!");
		}
		
		if(passwordHistory.getUserMaster() == null) {
			if(logger.isDebugEnabled()) logger.debug("User is null"+passwordHistory.getUserMaster());
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User not found!");
		}
	}

	@Override
	public void deleteOldestByUserId(int userId){
		
		if(logger.isDebugEnabled()) logger.debug("In password history deleteOldestByUserdss service method, deleting oldest record with userId-> "+userId);
		if(userId <= 0) 
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "User is invalid");
		
		passwordHistoryRepository.deleteOldestByUserId(userId);
	}
	
	@Override
	public void deleteById(int id){
		
		if(logger.isDebugEnabled()) logger.debug("In password history deleteId service method, deleting record with id-> "+id);
		if(id <= 0) 
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Id is invalid");
		
		passwordHistoryRepository.deleteById(id);
	}

}
