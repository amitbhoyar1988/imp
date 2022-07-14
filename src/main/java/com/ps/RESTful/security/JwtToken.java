package com.ps.RESTful.security;
import static java.util.Objects.requireNonNull;
public  class JwtToken {

    private  String token;

    public JwtToken(String json) {
        this.token = requireNonNull(json);
    }

	public String getToken() {
		return token;
	}
    public JwtToken() {
	}
    
}