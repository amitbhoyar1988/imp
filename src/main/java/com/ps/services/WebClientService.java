package com.ps.services;

import java.util.Map;

import org.springframework.http.ResponseEntity;

import reactor.core.publisher.Mono;

public interface WebClientService {

	public <T> Mono<ResponseEntity<String>> postNonBlocking(String url, T body);
		
	public <T> ResponseEntity<String> postBlocking(String url, T body);
	
	public <T> ResponseEntity<String> postBlocking(String url, T body, Map<String,String> headersMap);

}
