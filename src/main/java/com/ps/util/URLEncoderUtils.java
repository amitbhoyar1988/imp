package com.ps.util;

import java.util.Base64;

public class URLEncoderUtils {

	public static String encodeValue(String value) {
        try {
        	return Base64.getUrlEncoder().encodeToString(value.getBytes());
            //return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (Exception ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
}
