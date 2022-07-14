package com.ps.RESTful.security;

public interface TokenExtractor {
	  String extract(String payload);
}
