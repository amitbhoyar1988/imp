package com.ps.services;

import org.springframework.http.ResponseEntity;

public interface RestTemplateService {

	public <T> ResponseEntity<String> post(String url, T body);

}
