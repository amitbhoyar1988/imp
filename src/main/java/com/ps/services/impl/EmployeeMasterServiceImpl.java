package com.ps.services.impl;

import java.util.HashMap;
import java.util.Map;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientException;

import com.ps.RESTful.dto.request.EmployeeMasterRequestDTO;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.services.EmployeeService;
import com.ps.util.ApiPathConstants;
import com.ps.util.Constants;

@Service
public class EmployeeMasterServiceImpl implements EmployeeService{

	Logger logger = Logger.getLogger(EmployeeMasterServiceImpl.class);
	
	@Autowired
	RestTemplateServiceImpl restTemplateServiceImpl;
	
	@Autowired
	WebClientServiceImpl webClientServiceImpl;
	
	@Autowired
	Environment env;
	
	@Override
	public boolean verifyEmployeeDetails(EmployeeMasterRequestDTO employeeMasterDTO, String tenantId) {
		
		if(logger.isDebugEnabled()) logger.debug("In verifyEmployeeDetails Service method,"
				+ " calling employee service using webclient");
		
		String employeeMasterbaseUrl=env.getProperty("employee_master_service_base_URL").toString().trim();
		String applicationContextPath = env.getProperty("server.servlet.context-path").toString().trim();
		
		try {
			Map<String, String> headers = new HashMap<String, String>();
			headers.put(Constants.TENANT_ID_HEADER,tenantId);
					
			ResponseEntity<String> response = webClientServiceImpl.postBlocking(employeeMasterbaseUrl+applicationContextPath
									+ ApiPathConstants.EMPLOYEE_MASTER_BASE_URL
									+ ApiPathConstants.EMPLOYEE_MASTER_VERIFY_URL, employeeMasterDTO,headers);
			
			if(logger.isDebugEnabled()) logger.debug("In response response-> "+response);
			
			if(response.getStatusCode() == HttpStatus.OK) {			
				return true;
			}
		} catch (WebClientException wbException) {
			logger.error("verifyEmployeeDetails eexception: getMessage-> "+wbException.getMessage());			
			throw new InvalidRequestException(ErrorCode.BAD_REQUEST, "Employee details are invalid!");
		}		
		
		return false;
	}

}
