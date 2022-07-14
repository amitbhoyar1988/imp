package com.ps.RESTful.security;

import org.jboss.logging.Logger;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.stereotype.Service;

@Service
public final class JwtHeaderTokenExtractor
implements TokenExtractor {
	Logger logger = Logger.getLogger(JwtHeaderTokenExtractor.class);

    public static String HEADER_PREFIX = "Bearer ";

    @Override
    public String extract(String header) {
    	
    	if(logger.isDebugEnabled())
    		logger.debug("Extracting Token");
    	
        if (header == null || "".equals(header)) {
			logger.error("Authorization header cannot be blank!");

            throw new AuthenticationServiceException(
                "Authorization header cannot be blank!"
            );
        }

        if (header.length() < HEADER_PREFIX.length()) {
			logger.error("Invalid authorization header size.");

            throw new AuthenticationServiceException(
                "Invalid authorization header size."
            );
        }
        
        /*Before new Return:- was there
         * return header.substring(HEADER_PREFIX.length(), header.length());
         */
        
        //Replacing-HAEADER_PREFIX-from-token-and-returning-token
        return header.replace(HEADER_PREFIX,"").trim();
    }
}
