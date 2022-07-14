package com.ps.services.impl;

import java.util.List;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ps.dto.EmailDTO;
import com.ps.services.EmailService;
import com.ps.util.ApiPathConstants;

@Service
public class EmailServiceImpl implements EmailService {

	Logger logger = Logger.getLogger(EmailServiceImpl.class);

	@Autowired
	RestTemplateServiceImpl restTemplateServiceImpl;

	@Autowired
	WebClientServiceImpl webClientServiceImpl;

	@Autowired
	Environment env;

	@Override
	public void send(EmailDTO emailDTO) {

		if (logger.isDebugEnabled())
			logger.debug("In send email service,Calling emailservice using webclient");

		String emailServiceBasePath = env.getProperty("email_service_base_URL").toString().trim();
		String applicationContextPath = env.getProperty("server.servlet.context-path").toString().trim();

		if (logger.isDebugEnabled())
			logger.debug("URL" + emailServiceBasePath + applicationContextPath);
		
		webClientServiceImpl
				.postNonBlocking(emailServiceBasePath + applicationContextPath + ApiPathConstants.EMAIL_BASE_URL,
						emailDTO)
				.subscribe((data) -> {
					if (logger.isDebugEnabled())
						logger.debug("Successful Response " + data);
				}, (error) -> {
					if (logger.isDebugEnabled())
						logger.error("Error response " + error);
				});

	}

	@Override
	public void sendAll(List<EmailDTO> emailDTOList) {

		if (logger.isDebugEnabled())
			logger.debug("In send all method of email service," + " calling emailservice using webclient");

		String emailServiceBasePath = env.getProperty("email_service_base_URL").toString().trim();
		String applicationContextPath = env.getProperty("server.servlet.context-path").toString().trim();

		if (logger.isDebugEnabled())
			logger.debug("URL" + emailServiceBasePath + applicationContextPath);

		webClientServiceImpl.postNonBlocking(emailServiceBasePath + applicationContextPath
				+ ApiPathConstants.EMAIL_BASE_URL + ApiPathConstants.EMAIL_BATCH_URL, emailDTOList)
				.subscribe((data) -> {
					logger.error("Successful Response " + data);
				}, (error) -> {
					logger.error("Error response " + error);
				});
	}

}
