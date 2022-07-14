package com.ps.RESTful.security;

import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.ps.entities.master.GlobalUserMaster;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class JwtAuthenticationProviderTest {
	@InjectMocks
	JwtAuthenticationProvider jwtAuthenticationProvider ;
	
	@Mock
	JwtService jwtservice;
	@Mock
	Environment env;

	GlobalUserMaster user;
	
	 Claims claims;
	 JwtToken token;
		@Before
		public void setUp() throws Exception {
		String jwt = Jwts.builder()
		            .setSubject("1")
		            .setExpiration(new Date(System.currentTimeMillis() + 60000))
		            .signWith(SignatureAlgorithm.HS512, "secret")
		            .compact();
			claims = Jwts.parser()
		    .setSigningKey("secret")
		    .parseClaimsJws(jwt)
		    .getBody();
			 token=new JwtToken(jwt);
		}
	@Test
    public void authenticate()
    {
		List<String> scopes = new ArrayList<>();
		scopes.add("admin");
		claims.put("mail", "xyz@gmail.com");
		claims.put("scopes", scopes);
		Authentication auth = new JwtAuthentication(token, null);
		when(jwtservice.validate(Mockito.any())).thenReturn(claims);
		jwtAuthenticationProvider.authenticate(auth);

    }
}
