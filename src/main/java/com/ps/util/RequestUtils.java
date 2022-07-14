package com.ps.util;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;

import com.ps.RESTful.security.JwtService;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.services.GlobalUserMasterService;

@Component
public class RequestUtils {

	static Logger logger = Logger.getLogger(RequestUtils.class);

	@Autowired
	protected HttpServletRequest request;

	@Autowired
	JwtService jwtService;

	@Autowired
	GlobalUserMasterService userMater;

	@RequestScope
	public String getUserName() {

		if (logger.isDebugEnabled())
			logger.debug("In getUserName method retrieving username from request");

		if (jwtService.getClaims() != null) {

			String userName = new String();
			String emailId = new String();

			try {
				// Get-userName-and-email-id-by-subString-as-it-is-plain-object-can-not-use-JsonObject
				List<String> userData = Arrays.asList(jwtService.getClaims().get("UserDetails").toString().split(","));

				// Sorting-List-as-alphabetically
				Collections.sort(userData);

				// UserName-is-at-index-4
				userName = userData.get(4).split("=")[1].trim();

				// emailId-is-at-index-2
				emailId = userData.get(2).split("=")[1].trim();

			} catch (Exception e) {
				if (logger.isDebugEnabled())
					logger.debug("Failed to fetch user details from Token:" + e.getMessage());

				throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
						"Failed to fetch user detials from Token.");
			}

			if (logger.isDebugEnabled())
				logger.debug("UserName:" + userName + " & EmailId:" + emailId);

			// Return-UserName-or-emailId
			if (StringUtils.isValidString(userName) && !(userName.trim().equalsIgnoreCase("null")))
				return userName;
			else
				return emailId;

		}else {
			if (logger.isDebugEnabled())
				logger.debug("Token not found. Can not find user details.");

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Token not found. Can not find user details.");
		}
	}// getUserName-close

	public GlobalUserMaster getUser() {

		if (logger.isDebugEnabled())
			logger.debug("In getUser method retrieving user from request");

		Object user = request.getAttribute(Constants.USER_DETAILS);

		if (logger.isDebugEnabled())
			logger.debug("In getUser-> " + user);
		if (user != null)
			return (GlobalUserMaster) user;

		return null;
	}
}
