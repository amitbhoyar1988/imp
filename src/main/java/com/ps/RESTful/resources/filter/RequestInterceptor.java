package com.ps.RESTful.resources.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.security.JwtService;
import com.ps.RESTful.security.JwtToken;
import com.ps.config.tenant.TenantContextHolder;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.services.dao.repository.master.GlobalUserMasterRepository;
import com.ps.util.Constants;

import io.jsonwebtoken.ExpiredJwtException;

@Component
public class RequestInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LogManager.getLogger(RequestInterceptor.class);

	@Autowired
	Environment env;
	
	@Autowired
	GlobalUserMasterRepository userRepo;
	
	@Autowired
	JwtService jws;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object object) throws Exception {

		if (logger.isDebugEnabled())
			logger.debug("In RequestInterceptor, we are intercepting request-> " + request.getRequestURI());
		if (logger.isDebugEnabled())
			logger.debug("____________________________________________");

		String tenantId = request.getHeader("X-TenantID");
		if (logger.isDebugEnabled())
			logger.debug("RequestURI::" + request.getRequestURI() + " || Current X-TenantID-> " + tenantId);

		if (tenantId != null) {
			TenantContextHolder.setTenantId(tenantId);
		}
		final String userId;
		if (!authorisedRequest(request)) {
			String token = request.getHeader("X-Authorization");

			if (StringUtils.isBlank(token))
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Invalid Token String!");

			try {

				if (logger.isDebugEnabled())
					logger.debug("In Request Interceptor, Validating JWT token");

				userId = jws.validate(new JwtToken(token)).getSubject();

			} catch (ExpiredJwtException ex) {
				logger.error(String.format("Error occurred ", ex));
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "Your session is expired please login again");
			} catch (Exception ex) {
				logger.error(String.format("Error occurred :" + ex.getMessage(), ex));
				throw new InvalidRequestException(ErrorCode.UNAUTHORIZED, "You are not authorized!");
			}

			Optional<GlobalUserMaster> user = userRepo.findById(Integer.parseInt(userId));
			if (!user.isPresent()) {

				if (logger.isErrorEnabled())
					logger.error("Received invalid token for nonexisting user:" + userId);

				throw new RuntimeException("Received invalid token for nonexisting user '" + userId + "'.");
			}

			if (logger.isDebugEnabled())
				logger.debug("User found in database with id-> " + user.get().getUserMasterId());
			
			request.setAttribute(Constants.USER_NAME, user.get().getUserName());
			
			if (logger.isDebugEnabled())
				logger.debug("Setting user details in request for user with id-> " + user.get().getUserMasterId());
			
			request.setAttribute(Constants.USER_DETAILS, user.get());
		}
		return true;
	}

	private boolean authorisedRequest(HttpServletRequest request) {
		boolean isAuthorized = false;
		List<String> authorizedURLsList = new ArrayList<String>();
		authorizedURLsList.add("validate");
		authorizedURLsList.add("otp/resend");
		authorizedURLsList.add("email");
		authorizedURLsList.add("login");
		// only searching for users because all users end-point need
		// to be authorized without jwt as user does not require password for accessing
		// those end-points
		// and as user might not have password for setting/resetting password or for
		// verifying user
		/*
		 * This-is-Commented-by-MayurG-As-adding-new-user-details-must-be-with-JWT-token
		 * authorizedURLsList.add("users");
		 */
		for (String url : authorizedURLsList) {
			if (request.getRequestURI().contains(url)) {
				isAuthorized = true;
			}
		}
		return isAuthorized;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		if (logger.isDebugEnabled())
			logger.debug("In RequestInterceptor, post handle method clearing tenant from context for -> "
					+ request.getRequestURI());
		TenantContextHolder.clear();
	}

}
