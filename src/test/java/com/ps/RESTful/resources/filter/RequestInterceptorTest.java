package com.ps.RESTful.resources.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.RESTful.security.JwtService;
import com.ps.RESTful.security.JwtToken;
import com.ps.entities.master.OTPMaster;
import com.ps.entities.master.GlobalUserMaster;
import com.ps.services.dao.repository.master.GlobalUserMasterRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class RequestInterceptorTest {
	@InjectMocks
	RequestInterceptor requestInterceptor = new RequestInterceptor();

	@Mock
	Environment env;
	
	@Mock
	GlobalUserMasterRepository userRepo;
	
	@Mock
	JwtService jwtservice;
	
	 MockHttpServletRequest request = new MockHttpServletRequest();
	 MockHttpServletResponse response = new MockHttpServletResponse();
	Object object;
	GlobalUserMaster master;
	Optional<GlobalUserMaster> userOptional;
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
			request.setRequestURI("/verify");
			request.addHeader("X-Authorization",jwt);
			 token=new JwtToken(jwt);
			master = new GlobalUserMaster();
			userOptional = userOptional.of(master);
		}

	 // When request URL having login and token yet to be generated
@Test
 public void preHandleTest() throws Exception {
	request.setRequestURI("/login");
	boolean status = requestInterceptor.preHandle(request, response, object);
	assertEquals(true, status);
}

// Success scenario when token is valid
@Test
public void preHandleTest1() throws Exception {
	when(jwtservice.validate(Mockito.any(JwtToken.class))).thenReturn(claims);
	when(userRepo.findById(Mockito.anyInt())).thenReturn(userOptional);
	boolean status = requestInterceptor.preHandle(request, response, object);
	assertEquals(true, status);
}
// Exception expected when token is expired
@Test(expected = InvalidRequestException.class)
public void preHandleTest2() throws Exception {
	try {
	when(jwtservice.validate(Mockito.any(JwtToken.class))).thenThrow(new ExpiredJwtException(null, claims, null));
	requestInterceptor.preHandle(request, response, object);
	} catch(InvalidRequestException ex) {
		assertEquals(ErrorCode.BAD_REQUEST, ex.getErrorCode());
		assertEquals("Your session is expired please login again", ex.getDescription());
		throw ex;
	}
	
}

//Exception expected when token is invalid or null
@Test(expected = InvalidRequestException.class)
public void preHandleTest3() throws Exception {
	try {
	when(jwtservice.validate(Mockito.any(JwtToken.class))).thenThrow(new NullPointerException());
	requestInterceptor.preHandle(request, response, object);
	} catch(InvalidRequestException ex) {
		assertEquals(ErrorCode.BAD_REQUEST, ex.getErrorCode());
		assertEquals("You are not authorized!", ex.getDescription());
		throw ex;
	}
	
}

}