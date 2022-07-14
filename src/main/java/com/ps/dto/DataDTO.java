package com.ps.dto;

import java.util.List;

public class DataDTO implements DTO {
	
	private List<Object> results;
	
	
	public List<Object> getResults() {
		return results;
	}
	
	
	public void setResults(List<Object> results) {
		this.results = results;
	}

}
