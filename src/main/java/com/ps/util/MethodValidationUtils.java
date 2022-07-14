package com.ps.util;

import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;
import org.apache.commons.lang3.StringUtils;

import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.enums.ErrorCode;
import com.ps.dto.ErrorDTO;

public class MethodValidationUtils {

	static Logger logger = Logger.getLogger(MethodValidationUtils.class);

	public static void errorValidation(ErrorDTO error) {
		if (error != null) {
			logger.error(error.getMessage());
			throw new InvalidRequestException(error.getCode(), error.getMessage());
		}
	}

	public static <T> void checkIfObjectIsNotNULL(T obj, String className) {
		if (obj == null) {
			logger.error(className + " details is null");
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, className + " Details is null");
		}
	}

	public static <T> void checkIfDatabaseObjectIsNotNULL(T obj, String className) {
		if (obj == null) {
			logger.error(className + " details Not Found");
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, className + " Details Not Found");
		}

	}

	public static <T> void checkIfObjectListIsNotEmpty(List<T> obj, String className) {
		if (obj.isEmpty()) {
			logger.error(className + " details list is empty");
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, className + " Details list is empty");
		}
	}

	public static <T> void checkIfDatabaseObjectListIsNotEmpty(List<T> obj, String className) {
		if (obj.isEmpty()) {
			logger.error(className + " details list is not found");
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, className + " Details list is not found");
		}
	}

	public static void checkIfIdIsZero(int id, String idName) {
		if (id == 0) {
			logger.error(idName + " is null. ");
			throw new InvalidRequestException(ErrorCode.INVALID_PARAMETER, idName + " is invalid!");
		}
	}
	
	public static <T> void checkIfObjectIsPresent(Optional<T> obj, String className) {
		if (!obj.isPresent()) {
			logger.error(className + " details Not Found");
			throw new InvalidRequestException(ErrorCode.RESOURCE_NOT_FOUND, className + " details Not Found");
		}
	}	
	
	public static boolean isStringsEqual(String string1, String string2) {
		if (StringUtils.equalsIgnoreCase(string1, string2)) {
			if (logger.isDebugEnabled())
				logger.debug("In isStringsEqual utility method: String are equal:");
			return true;
		}

		if (logger.isDebugEnabled())
			logger.debug("In isStringsEqual utility method: String are not equal, string 1:" + string1 + "& string 2:" + string2);

		return false;
	}
}
