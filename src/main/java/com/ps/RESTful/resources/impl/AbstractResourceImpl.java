package com.ps.RESTful.resources.impl;

import org.jboss.logging.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;

import com.ps.RESTful.resources.AbstractResource;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.util.ErrorMessageConstants;

//every resource implementing this class will follow a standard format of get/delete method 
//common methods for accessing request can be made here which will be accessible in all the resources
//this approach promotes reusability of code as there will be common methods which will prevent resource pollution 
public abstract class AbstractResourceImpl implements AbstractResource {

	static Logger logger = Logger.getLogger(AbstractResourceImpl.class);
	
	public ResponseEntity<Response> get(@PathVariable("resourceId") int resourceId){
		 throw new UnsupportedOperationException(ErrorMessageConstants.ERROR_METHOD_NOT_IMPLEMENTED);		
	}	
	
	public ResponseEntity<Response> delete(@PathVariable("resourceId") int resourceId){
		 throw new UnsupportedOperationException(ErrorMessageConstants.ERROR_METHOD_NOT_IMPLEMENTED);		
	}
	

}
