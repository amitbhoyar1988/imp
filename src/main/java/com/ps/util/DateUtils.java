package com.ps.util;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.jboss.logging.Logger;

public class DateUtils {

	static Logger logger = Logger.getLogger(DateUtils.class);
	
	static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
	
	public static Date getDate(String inputDate){
		
		if(logger.isDebugEnabled()) logger.debug("In getDate utility method "
				+ "converting string to date inputDate-> "+inputDate);	
		
		Date date = null;
		try {			
			if(!StringUtils.isBlank(inputDate)) {
				if(logger.isDebugEnabled()) logger.debug("Parsing the input date string "+inputDate);
				date = dateFormatter.parse(inputDate);
			}			
		} catch (Exception e) {
			logger.error("Exception occured while "
					+ "converting string to date "+e.getMessage(),e);			
			e.printStackTrace();
		}
		
		if(logger.isDebugEnabled()) logger.debug("Returning date object after conversion"
				+ " date object-> "+date);	
		
		return date;
	}
	
	public static String getDateString(Date inputDate){
		
		if(logger.isDebugEnabled()) logger.debug("In getDateString utility method "
				+ "converting date to string inputDate-> "+inputDate);	
		
		String date = null;
		try {			
			if(logger.isDebugEnabled()) logger.debug("Formatting the input date object "+inputDate);
			date = dateFormatter.format(inputDate);			
		} catch (Exception e) {
			logger.error("Exception occured while "
					+ "converting date to string "+e.getMessage(),e);			
			e.printStackTrace();
		}
		
		if(logger.isDebugEnabled()) logger.debug("Returning string date after conversion"
				+ " date String-> "+date);	
		
		return date;
	}
	
}
