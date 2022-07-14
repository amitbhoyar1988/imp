/*
 * package com.ps.services.impl;
 * 
 * import static org.junit.Assert.assertNotNull; import static
 * org.junit.jupiter.api.Assertions.assertEquals; import static
 * org.mockito.Mockito.doNothing; import static org.mockito.Mockito.when;
 * 
 * import java.time.LocalDateTime; import java.time.ZoneId; import
 * java.util.ArrayList; import java.util.Date; import java.util.List; import
 * java.util.Optional;
 * 
 * import org.junit.Before; import org.junit.Test; import
 * org.junit.runner.RunWith; import org.mockito.InjectMocks; import
 * org.mockito.Mock; import org.mockito.Mockito; import
 * org.mockito.MockitoAnnotations; import org.mockito.junit.MockitoJUnitRunner;
 * import org.springframework.boot.test.context.SpringBootTest; import
 * org.springframework.core.env.Environment;
 * 
 * import com.ps.RESTful.enums.ErrorCode; import
 * com.ps.RESTful.error.handler.InvalidRequestException; import
 * com.ps.RESTful.security.JwtServiceImpl; import
 * com.ps.RESTful.security.JwtToken; import com.ps.beans.JWTBean; import
 * com.ps.dto.EmailDTO; import com.ps.entities.master.OTPMaster; import
 * com.ps.entities.master.GlobalUserMaster; import com.ps.services.EmailService;
 * import com.ps.services.dao.repository.master.OTPDetailsRepository; import
 * com.ps.services.dao.repository.master.OtpFailedTransactionRepository; import
 * com.ps.services.dao.repository.master.GlobalUserMasterRepository; import
 * com.ps.util.SMSSenderUtils;
 * 
 * @RunWith(MockitoJUnitRunner.class)
 * 
 * @SpringBootTest public class OTPDetailsServiceImplTest {
 * 
 * @InjectMocks OTPDetailsServiceImpl detailsServiceImpl = new
 * OTPDetailsServiceImpl();
 * 
 * @Mock SMSSenderUtils smsService;
 * 
 * @Mock JwtServiceImpl jstService;
 * 
 * @Mock GlobalUserMasterRepository userDetailsRepositor;
 * 
 * @Mock OTPDetailsRepository otpDetailsRepository;
 * 
 * @Mock OtpFailedTransactionRepository otpFailedTransRepo;
 * 
 * @Mock Environment env;
 * 
 * @Mock EmailService emailService;
 * 
 * GlobalUserMaster data; GlobalUserMaster master; Optional<GlobalUserMaster>
 * userOptionaldata; OTPMaster otpData; Optional<GlobalUserMaster> userOptional
 * ;
 * 
 * @Before public void setUp() throws Exception { data = new GlobalUserMaster();
 * data.setIsLocked(false); data.setActive(true);
 * data.setMobileNumber("9623203232"); data.setEmailId("xyz@gmail.com"); master
 * = new GlobalUserMaster(); userOptionaldata = Optional.of(master);
 * userOptional = Optional.of(data); otpData = new OTPMaster();
 * otpData.setUserOTPAttempt(0); otpData.setOtpNumber(123456);
 * otpData.setIsOTPUsed(0); LocalDateTime ldt = LocalDateTime.now();
 * otpData.setCreateDateTime(Date.from(ldt.atZone(ZoneId.systemDefault()).
 * toInstant())); ldt = ldt.plusMinutes(10);
 * otpData.setExpiryDateTime(Date.from(ldt.atZone(ZoneId.systemDefault()).
 * toInstant())); MockitoAnnotations.initMocks(this);
 * 
 * }
 * 
 * //Testing SMS sent successfully scenario
 * 
 * @Test public void generateAndSaveOTPTest() {
 * when(smsService.send(Mockito.anyString(),
 * Mockito.anyString())).thenReturn(true);
 * doNothing().when(emailService).send(Mockito.any(EmailDTO.class)); OTPMaster
 * otpMaster = new OTPMaster(); otpMaster.setIsActive(1); List<OTPMaster> list =
 * new ArrayList(); list.add(otpMaster);
 * when(otpDetailsRepository.activeStatusList(Mockito.anyString(),
 * Mockito.anyString())).thenReturn(list);
 * when(otpDetailsRepository.save(Mockito.any(OTPMaster.class))).thenReturn(
 * otpMaster); when(env.getProperty(Mockito.anyString())).thenReturn("10");
 * boolean status = detailsServiceImpl.generateAndSaveOTP(data); // Expected
 * true value assertEquals(true, status); } //Testing SMS not sent successfully
 * scenario return false
 * 
 * @Test public void generateAndSaveOTPTest1() {
 * when(smsService.send(Mockito.anyString(),
 * Mockito.anyString())).thenReturn(false);
 * doNothing().when(emailService).send(Mockito.any(EmailDTO.class)); OTPMaster
 * otpMaster = new OTPMaster();
 * when(otpDetailsRepository.save(Mockito.any(OTPMaster.class))).thenReturn(
 * otpMaster); when(env.getProperty(Mockito.anyString())).thenReturn("10");
 * boolean status = detailsServiceImpl.generateAndSaveOTP(data);
 * assertEquals(false, status); }
 * 
 * // expecting exception when user is inactive
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * generateAndSaveOTPTest2() { GlobalUserMaster data = new GlobalUserMaster();
 * data.setActive(false); try { detailsServiceImpl.generateAndSaveOTP(data); }
 * catch (InvalidRequestException exception) {
 * assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
 * assertEquals("User is not active", exception.getDescription()); throw
 * exception; } }
 * 
 * // expecting exception when User is locked
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * generateAndSaveOTPTest3() { data.setIsLocked(true); try {
 * detailsServiceImpl.generateAndSaveOTP(data); } catch (InvalidRequestException
 * exception) { assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
 * assertEquals("User is locked", exception.getDescription()); throw exception;
 * } } // Validate OTP - Success scenario
 * 
 * @Test public void validateOTPTest() {
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(otpData);
 * when(userDetailsRepositor.findByEmailIdOrMobileNumberAndIsActive(otpData.
 * getEmailId(), otpData.getMobileNumber(), true))
 * .thenReturn(userOptionaldata);
 * when(env.getProperty(Mockito.anyString())).thenReturn("3"); JwtToken token =
 * new JwtToken("TestToken");
 * when(jstService.createLoginToken(Mockito.any(GlobalUserMaster.class))).
 * thenReturn(token); JWTBean tokenResp =
 * detailsServiceImpl.validateOTP(otpData); assertNotNull(tokenResp);
 * assertEquals("TestToken", tokenResp.getToken());
 * 
 * } // OTP Expired expecting exception
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * validateOTPTest1() { LocalDateTime ldt = LocalDateTime.now();
 * otpData.setCreateDateTime(Date.from(ldt.atZone(ZoneId.systemDefault()).
 * toInstant())); ldt = ldt.minusMinutes(10);
 * otpData.setExpiryDateTime(Date.from(ldt.atZone(ZoneId.systemDefault()).
 * toInstant()));
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(otpData); try {
 * detailsServiceImpl.validateOTP(otpData); } catch (InvalidRequestException
 * exception) { assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
 * assertEquals("OTP is expired", exception.getDescription()); throw exception;
 * } }
 * 
 * // expecting exception when otp already used..
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * validateOTPTest2() {
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(otpData); otpData.setIsOTPUsed(1);
 * try { detailsServiceImpl.validateOTP(otpData); } catch
 * (InvalidRequestException exception) { assertEquals(ErrorCode.BAD_REQUEST,
 * exception.getErrorCode()); assertEquals("OTP is already used",
 * exception.getDescription()); throw exception; } }
 * 
 * // expecting exception when Is validation attempt more than 3
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * validateOTPTest3() {
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(otpData);
 * when(env.getProperty(Mockito.anyString())).thenReturn("3");
 * otpData.setUserOTPAttempt(3); try { detailsServiceImpl.validateOTP(otpData);
 * } catch (InvalidRequestException exception) {
 * assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
 * assertEquals("Maximum limit exceed", exception.getDescription()); throw
 * exception; } } // expecting exception when token is not generating
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * validateOTPTest4() {
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(otpData);
 * when(userDetailsRepositor.findByEmailIdOrMobileNumberAndIsActive(otpData.
 * getEmailId(), otpData.getMobileNumber(),true)) .thenReturn(userOptionaldata);
 * when(env.getProperty(Mockito.anyString())).thenReturn("3"); JwtToken token =
 * new JwtToken("TestToken");
 * when(jstService.createLoginToken(Mockito.any(GlobalUserMaster.class))).
 * thenReturn(null); try { detailsServiceImpl.validateOTP(otpData); } catch
 * (InvalidRequestException exception) { assertEquals(ErrorCode.BAD_REQUEST,
 * exception.getErrorCode()); assertEquals("Invalid User Id and OTP",
 * exception.getDescription()); throw exception; } }
 * 
 * //expecting exception when OTP is Invalid OTP
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * validateOTPTest5() { otpData.setOtpNumber(123431);
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(otpData);
 * when(env.getProperty(Mockito.anyString())).thenReturn("3"); try { OTPMaster
 * master = new OTPMaster(); master.setOtpNumber(123131);
 * detailsServiceImpl.validateOTP(master); } catch (InvalidRequestException
 * exception) { assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
 * throw exception; } } //expecting exception whenProvided details are Invalid
 * 
 * @Test(expected = InvalidRequestException.class) public void
 * validateOTPTest6() {
 * when(otpDetailsRepository.findOTPByEmailIdOrMobile(otpData.getEmailId(),
 * otpData.getMobileNumber())) .thenReturn(null); try {
 * detailsServiceImpl.validateOTP(otpData); } catch (InvalidRequestException
 * exception) { assertEquals(ErrorCode.BAD_REQUEST, exception.getErrorCode());
 * assertEquals("Provided details are Invalid", exception.getDescription());
 * throw exception; } } // Resend OTP success scenario
 * 
 * @Test public void resendOTP() {
 * when(userDetailsRepositor.findByEmailIdOrMobileNumberAndIsActive(Mockito.
 * anyString(), Mockito.anyString(), Mockito.anyBoolean()))
 * .thenReturn(userOptional);
 * when(env.getProperty(Mockito.anyString())).thenReturn("3");
 * when(smsService.send(Mockito.anyString(),
 * Mockito.anyString())).thenReturn(true);
 * doNothing().when(emailService).send(Mockito.any(EmailDTO.class)); OTPMaster
 * otpMaster = new OTPMaster(); otpMaster.setIsActive(1); List<OTPMaster> list =
 * new ArrayList(); list.add(otpMaster);
 * when(otpDetailsRepository.activeStatusList(Mockito.anyString(),
 * Mockito.anyString())).thenReturn(list);
 * when(otpDetailsRepository.save(Mockito.any(OTPMaster.class))).thenReturn(
 * otpMaster); OTPMaster t = new OTPMaster(); t.setMobileNumber("9623203232");
 * t.setEmailId("xyz@gmail.com"); String response =
 * detailsServiceImpl.resend(t); assertEquals("Otp sent successfully",
 * response); }
 * 
 * //expecting exception whenProvided details are Invalid
 * 
 * @Test(expected = InvalidRequestException.class) public void resendOTP1() {
 * try { otpData.setMobileNumber("9623203232");
 * detailsServiceImpl.resend(otpData); } catch (InvalidRequestException
 * exception) { assertEquals(ErrorCode.UNAUTHORIZED, exception.getErrorCode());
 * assertEquals("Provide user deatils are invalid", exception.getDescription());
 * throw exception; } } }
 */