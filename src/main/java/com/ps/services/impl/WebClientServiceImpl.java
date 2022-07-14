package com.ps.services.impl;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

import com.ps.services.WebClientService;

import reactor.core.publisher.Mono;

@Service
public class WebClientServiceImpl implements WebClientService{

	@Autowired
	WebClient webclient;

	@Override
	public <T> Mono<ResponseEntity<String>> postNonBlocking(String url, T body) {

		return webclient.post()
				.uri(url)
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.toEntity(String.class);
	}

	@Override
	public <T> ResponseEntity<String> postBlocking(String url, T body) {
		return webclient.post()
				.uri(url)
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.toEntity(String.class)
				.block();
	}
	
	@Override
	public <T> ResponseEntity<String> postBlocking(String url, T body, Map<String,String> headersMap) {
				
		return webclient.post()
				.uri(url)
				.headers(headers -> {
					headersMap.forEach((key,value)->{
						headers.add(key, value);
					});					
				})
				.body(BodyInserters.fromValue(body))
				.retrieve()
				.toEntity(String.class)
				.block();
	}

}
