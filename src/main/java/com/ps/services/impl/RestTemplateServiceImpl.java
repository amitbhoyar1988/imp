package com.ps.services.impl;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.ps.services.RestTemplateService;

@Service
public class RestTemplateServiceImpl implements RestTemplateService{

	@Autowired
	RestTemplate restTemplate;

	@Override
	public <T> ResponseEntity<String> post(String url, T body) {

		HttpHeaders headers = new HttpHeaders();
	      headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
	      HttpEntity<T> entity = new HttpEntity<T>(body,headers);
	      
	      return restTemplate
	    		  .exchange(url, HttpMethod.POST,entity, String.class);
	}

}
