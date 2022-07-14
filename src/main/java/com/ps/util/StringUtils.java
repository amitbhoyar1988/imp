package com.ps.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import org.jboss.logging.Logger;

import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.enums.ErrorCode;

public class StringUtils {

  static Logger logger = Logger.getLogger(StringUtils.class);

  public static boolean isValidString(String stringToValidate) {
    if (stringToValidate != null && !stringToValidate.isEmpty() && stringToValidate.length()>0) {
      if (logger.isDebugEnabled())
        logger.debug("In isValidString utility method: String is Valid:");
      return true;
    }

    if (logger.isDebugEnabled())
      logger.debug("In isValidString utility method: String is Invalid:" + stringToValidate);

    return false;
  }

  public static Date stringToDate(String simpleString) {
    if (logger.isDebugEnabled())
      logger.debug("In stringToDate utility method " + "converting string: " + simpleString);
    if (simpleString == null) {
      return null;
    }
    Date date = null;
    if (!simpleString.isEmpty()) {
      DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);

      try {
        date = format.parse(simpleString);

      } catch (ParseException error) {
        logger.error(error.getMessage());
        throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, simpleString+": Date formate is wrong");
      }
      if (logger.isDebugEnabled())
        logger.debug("converted date:" + date);

    }
    return date;
  }

  public static String dateToString(Date date) {
    if (logger.isDebugEnabled())
      logger.debug("In dateToString utility method " + "converting date: " + date);
    String strDate = null;
    if (date != null) {
      DateFormat format = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
      strDate = format.format(date);

      if (logger.isDebugEnabled())
        logger.debug("converted date:" + strDate);

    }
    return strDate;
  }
  
  
  public static boolean checkStartDateLessThanEndDate(Date inputDate1, Date inputDate2){
    
    if(logger.isDebugEnabled()) logger.debug("In checkStartDateLessThanEndDate utility method "
        + "checking for StartDate-> "+inputDate1 +" and EndDate-> "+inputDate2 ); 
    
    boolean result = true;
    try {     
      if (inputDate1.compareTo(inputDate2) > 0) {
        result = false;
      } 
    } catch (Exception e) {
      logger.error("Exception occured while "
          + "comparision between dates "+e.getMessage(),e);     
      e.printStackTrace();
    }
    
    if(logger.isDebugEnabled()) logger.debug("Returning result after comparision"
        + "checking for StartDate-> "+inputDate1 +" and EndDate-> "+inputDate2);  
    
    return result;
  }
  
  public static boolean isFutureDate(Date inputDate){
      
      if(logger.isDebugEnabled()) logger.debug("In isFutureDate utility method "
          + "checking for Date-> "+inputDate ); 
      
      boolean result = false;
      Date current = new Date();
      try {   
       if(!inputDate.before(current)){
         result = true;
           }
      } catch (Exception e) {
        logger.error("Exception occured while "
            + "checking for Future Date "+e.getMessage(),e);      
        e.printStackTrace();
      }   
      if(logger.isDebugEnabled()) logger.debug("Returning result after checking for Future Date"
          + "checking for Date-> "+inputDate );     
      return result;
    } 
  
  public static Date currentDate() {
    String currDate =  null;
    Date dtCurrDate = new Date();
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
                        
    try{
      currDate = formatter.format(dtCurrDate);
      dtCurrDate = StringUtils.stringToDate(currDate);
      }
     catch (Exception e) {
      logger.error("Exception occured while fetching current Date "+e.getMessage(),e);      
      e.printStackTrace();
    } 
    return dtCurrDate;
  }
  
  public static Date convertToddMMMyyyy(Date date) {
    String currDate =  null;
    SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
    
    try{
     currDate = formatter.format(date);
     date = StringUtils.stringToDate(currDate);
       }
     catch (Exception e) {
      logger.error("Exception occured while converting date to dd-MMMM-yyyy formate "+e.getMessage(),e);      
      e.printStackTrace();
    } 
    return date;
  }
  
  public static Date addOneDayToDate(Date date) {
        
    try{
      Calendar c = Calendar.getInstance();
          c.setTime(date);
          c.add(Calendar.DATE, 1);
          date = c.getTime();
       }
     catch (Exception e) {
      logger.error("Exception occured while adding 1 day into the date "+e.getMessage(),e);     
      e.printStackTrace();
    } 
    return date;
  }
  
  

}