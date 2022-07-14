package com.ps.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.ResourceNotFoundException;

@Component
public class SMSSenderUtils {
	Logger logger = Logger.getLogger(SMSSenderUtils.class);
	@Autowired
	Environment env;

	public boolean send(String messageBody, String mobile) {
		logger.info("Inside sendOTPOnMobile() ");
		try {
			final String user = env.getProperty("sms.gateway.username");
			final String password = env.getProperty("sms.gateway.password");
			// final String sid = "paysquare";
			String vendor = env.getProperty("sms.gatway.url");
			String templateId = env.getProperty("sms.template.dlt.id");

			final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; WOW64; rv:52.0) Gecko/20100101 Firefox/52.0";
			String strUrl = "https://www.businesssms.co.in/smsaspx";
			StringBuffer resp = new StringBuffer();
//			String messageBody = "Your One Time Password to login is : " + otp;

			URL obj = new URL(vendor);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();
			// add reuqest header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
			con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

			String urlParameters = "ID=" + URLEncoder.encode(user, "UTF-8") + "&Pwd="
					+ URLEncoder.encode(password, "UTF-8") + "&PhNo=" + URLEncoder.encode(mobile, "UTF-8") + "&Text="
					+ URLEncoder.encode(messageBody, "UTF-8") + "&TemplateID=" + URLEncoder.encode(templateId, "UTF-8");

			// Send post request
			con.setDoOutput(true);

			try {
				DataOutputStream wr = new DataOutputStream(con.getOutputStream());
				wr.writeBytes(urlParameters);
				wr.flush();
				wr.close();

			} catch (Exception e) {
				
				if(logger.isDebugEnabled())
					logger.debug("Failed to send OTP via SMS. Exception:"+e.getMessage());
				
				return false;
			}
			

			int responseCode = con.getResponseCode();
			String responseMsg = con.getResponseMessage();
			if (logger.isDebugEnabled()) {
				logger.debug("Sending 'POST' request to URL : " + strUrl);
				logger.debug("Post parameters : " + urlParameters);
				logger.debug("Response Code : " + responseCode);
				logger.debug("Response msg : " + responseMsg);
			}

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			String retval = "";
			while ((inputLine = in.readLine()) != null) {
				resp.append(inputLine);
				retval += resp.toString();
			}
			in.close();
			if (responseCode == 200 && retval.trim().equals("Message Submitted")) {
				logger.info("OTP sent on mobile - " + mobile);
				return true;
			}
			System.out.println("Response is  - > " + retval);
		} catch (Exception e) {
			e.printStackTrace();
			logger.error("Error Occured during Sending sms on mobile " + e.getStackTrace());
			throw new ResourceNotFoundException(ErrorCode.INTERNAL_SERVER_ERROR,
					"Something went wrong please try later");
		}
		logger.info("Exit sendOTPOnMobile() ");
		return false;
	}
}
