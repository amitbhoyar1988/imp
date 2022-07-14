package com.ps.RESTful.dto.response;

public class JwtResponseDTO {

	private String token;
	private GlobalUserMasterResponseDTO user;
	
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public GlobalUserMasterResponseDTO getUser() {
		return user;
	}
	public void setUser(GlobalUserMasterResponseDTO user) {
		this.user = user;
	}
	
}
