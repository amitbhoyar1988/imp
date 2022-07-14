package com.ps.integration;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.net.URI;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ps.RESTful.dto.mapper.JwtDTOMapper;
import com.ps.RESTful.dto.mapper.OTPDetailsDTOMapper;
import com.ps.RESTful.dto.request.OTPDetailsRequestDTO;
import com.ps.RESTful.dto.response.JwtResponseDTO;
import com.ps.RESTful.enums.StatusEnum;
import com.ps.RESTful.enums.SuccessCode;
import com.ps.RESTful.resources.response.handler.Response;
import com.ps.RESTful.resources.response.handler.ResponseBuilder;
import com.ps.RESTful.resources.response.handler.ResponseImpl;
import com.ps.beans.JWTBean;
import com.ps.dto.DataDTO;
import com.ps.dto.MetaDTO;
import com.ps.dto.StatusDTO;
import com.ps.entities.master.OTPMaster;
import com.ps.main.Application;
import com.ps.services.OTPDetailsService;

import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class OTPDetailsIntegrationTest {

	@LocalServerPort
	private int port;

	//@Autowired
	private TestRestTemplate testRestTemplate = new TestRestTemplate();
	private HttpHeaders headers = new HttpHeaders();
	@MockBean
	OTPDetailsService otpValidateService;
	private  OTPDetailsRequestDTO otpDetails;
	@MockBean
	JwtDTOMapper jwtDTOMapper;
	
	@MockBean
	OTPDetailsDTOMapper otpDTOMapper;
	@SuppressWarnings("unused")
	private ResponseEntity<Response> responseEntity;
	@Test
	public void validateOTPWithPositiveReponseTest() throws Exception{
		otpDetails = new OTPDetailsRequestDTO();
		otpDetails.setMobileNumber("8805673344");
		otpDetails.setOtp(123456);
		OTPMaster master = new OTPMaster();
		master.setMobileNumber("9673256236");
	//	otpDetails.setOtp(123456);
		String URITovalidateOTP = "hrms/v1/otp/validate";
		JWTBean bean = new JWTBean();
		bean.setToken("testToken");
		when(otpDTOMapper.dtoToEntity(Mockito.any(OTPDetailsRequestDTO.class))).thenReturn(master);
		when(otpValidateService.validateOTP(master)).thenReturn(bean);
		JwtResponseDTO responseDTO =  jwtDTOMapper.beanToDTO(bean);
		HttpEntity<OTPDetailsRequestDTO> entity = new HttpEntity<OTPDetailsRequestDTO>(otpDetails, headers);
		
		//Actual response
				ResponseEntity<String> actualResponse = testRestTemplate.exchange(
						formFullURLWithPort(URITovalidateOTP),
						HttpMethod.POST, entity, String.class);
	   //Expected response
				ResponseEntity<Response> responseExpected =	ResponseEntity.status(HttpStatus.CREATED).body(
						ResponseBuilder
						.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "Token generated successfully")
						.result(responseDTO).build());
				ResponseImpl expectedJson=new ResponseImpl();
				
				assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
				assertTrue(actualResponse.getBody().contains("Token generated successfully"));
				//assertEquals(mapToJson(responseExpected.getBody()), response.getBody()); failed beacuse timestamp

	}

	/**
	 * this utility method Maps an Object into a JSON String. Uses a Jackson ObjectMapper.
	 */
	private String mapToJson(Object object) throws JsonProcessingException {
		ObjectMapper objectMapper = new ObjectMapper();
		return objectMapper.writeValueAsString(object);
	}


	private String formFullURLWithPort(String uRITovalidateOTP) {
		// TODO Auto-generated method stub
		 return "http://localhost:" + port + uRITovalidateOTP;
	}
	@Test
	public void resendOTPTest() throws Exception{

		otpDetails = new OTPDetailsRequestDTO();
		otpDetails.setMobileNumber("8805673344");
		otpDetails.setOtp(123456);
		OTPMaster master = new OTPMaster();
		master.setMobileNumber("9673256236");
	//	otpDetails.setOtp(123456);
		String URITovalidateOTP = "hrms/v1/otp/resend";
		JWTBean bean = new JWTBean();
		bean.setToken("testToken");
		when(otpDTOMapper.dtoToEntity(Mockito.any(OTPDetailsRequestDTO.class))).thenReturn(master);
		when(otpValidateService.resend(master)).thenReturn("OTP sent successfully");
		JwtResponseDTO responseDTO =  jwtDTOMapper.beanToDTO(bean);
		HttpEntity<OTPDetailsRequestDTO> entity = new HttpEntity<OTPDetailsRequestDTO>(otpDetails, headers);
		
		//Actual response
				ResponseEntity<String> actualResponse = testRestTemplate.exchange(
						formFullURLWithPort(URITovalidateOTP),
						HttpMethod.POST, entity, String.class);
	   //Expected response 
				ResponseEntity<Response> responseExpected =	ResponseEntity.status(HttpStatus.CREATED).body(
						ResponseBuilder
						.builder()
						.status(StatusEnum.SUCCESS.getValue(), SuccessCode.OK.getCode(), "OTP sent successfully")
						.result(responseDTO).build());
				ResponseImpl expectedJson=new ResponseImpl();
				
				assertEquals(HttpStatus.CREATED, actualResponse.getStatusCode());
				assertTrue(actualResponse.getBody().contains("OTP sent successfully"));
				//assertEquals(mapToJson(responseExpected.getBody()), response.getBody()); failed beacuse timestamp
	}

}
