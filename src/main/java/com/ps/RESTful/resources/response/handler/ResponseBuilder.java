package com.ps.RESTful.resources.response.handler;

import java.util.ArrayList;
import java.util.List;

import com.ps.dto.DataDTO;
import com.ps.dto.MetaDTO;
import com.ps.dto.StatusDTO;

public class ResponseBuilder {
	private List<Object> results = new ArrayList<Object>();
	private StatusDTO status = new StatusDTO();	
	
	/*
	 * @Autowired private RequestUtils requestUtil;
	 */
	
	private ResponseBuilder() {}
	
	public static ResponseBuilder builder() {
		ResponseBuilder builder = new ResponseBuilder();
		return builder;
	}

	public ResponseBuilder results(List results) {
		if (results != null) {
			this.results.addAll(results);
		}
		return this;
	}
	
	public ResponseBuilder result(Object result) {		
		if (results != null) {
			this.results.add(result);
		}
		return this;		
	}
	
	public ResponseBuilder status(String result,String code,String message) {
		this.status.setResult(result);
		this.status.setCode(code);
		this.status.setMessage(message);
		return this;
	}
	
	public ResponseBuilder status(StatusDTO status) {
		this.status = status;
		return this;
	}
	
	
	public Response build() {
		DataDTO dataDTO = new DataDTO();		
		dataDTO.setResults(results);
				
		MetaDTO metaDTO = new MetaDTO();
		metaDTO.setTimestamp(System.currentTimeMillis());
		//metaDTO.setUser(requestUtil.getUserName());
		
		Response response = new ResponseImpl();
		response.setData(dataDTO);
		response.setMeta(metaDTO);
		response.setStatus(status);
		
		return response;
	}
}
