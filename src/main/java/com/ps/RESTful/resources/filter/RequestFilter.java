package com.ps.RESTful.resources.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Order;
import org.springframework.stereotype.Component;

@Component
@Order(1)
public class RequestFilter implements Filter  {
		
	  private static final Logger logger = LogManager.getLogger(RequestFilter.class);
	  
	  @Override 
	  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {  
	  
	  if(logger.isDebugEnabled())logger.debug("In request filter We are filtering the Request");
	  if(logger.isDebugEnabled())logger.debug("____________________________________________");
	  
	  chain.doFilter(request, response); 
	  
	  }
	  
	/*
	 * private byte[] formatResponse(Response response) throws IOException { String
	 * serealized = new ObjectMapper().writeValueAsString(response); return
	 * serealized.getBytes(); }
	 */

}

	
	
