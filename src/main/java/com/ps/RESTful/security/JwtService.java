package com.ps.RESTful.security;

import com.ps.entities.master.GlobalUserMaster;

import io.jsonwebtoken.Claims;

public interface JwtService {

	JwtToken createLoginToken(GlobalUserMaster user);

	/*
	 * it was like: JwtToken createLoginToken(GlobalUserMaster user,
	 * List<EmployeeRoleAssingment> roles);
	 */

	Claims validate(JwtToken token);

	Claims getClaims();
}
