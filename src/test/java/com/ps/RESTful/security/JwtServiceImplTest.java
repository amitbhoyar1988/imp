/*
 * package com.ps.RESTful.security;
 * 
 * import static org.junit.Assert.assertNotNull; import static
 * org.mockito.Mockito.when;
 * 
 * import org.junit.Test; import org.junit.runner.RunWith; import
 * org.mockito.InjectMocks; import org.mockito.Mock; import org.mockito.Mockito;
 * import org.mockito.junit.MockitoJUnitRunner; import
 * org.springframework.boot.test.context.SpringBootTest; import
 * org.springframework.core.env.Environment;
 * 
 * import com.ps.entities.master.GlobalUserMaster;
 * 
 * import io.jsonwebtoken.Claims;
 * 
 * @RunWith(MockitoJUnitRunner.class)
 * 
 * @SpringBootTest public class JwtServiceImplTest {
 * 
 * @InjectMocks JwtServiceImpl jwtServiceImpl;
 * 
 * @Mock JwtService jwtservice;
 * 
 * @Mock Environment env;
 * 
 * GlobalUserMaster user; JwtToken token;
 * 
 * @Test public void createLoginTokenTest() { user = new GlobalUserMaster();
 * user.setUserMasterId(1);
 * when(env.getProperty("service.jwt.issuer")).thenReturn("http://paysquare.com"
 * ); when(env.getProperty("service.jwt.expiration")).thenReturn("600000");
 * when(env.getProperty("service.jwt.secret")).thenReturn("d://private_key.der")
 * ; JwtToken t = jwtServiceImpl.createLoginToken(user); assertNotNull(t);
 * 
 * }
 * 
 * 
 * @Test public void validateTest() { user = new GlobalUserMaster();
 * user.setUserMasterId(1);
 * when(env.getProperty("service.jwt.issuer")).thenReturn("http://paysquare.com"
 * ); when(env.getProperty("service.jwt.expiration")).thenReturn("600000");
 * when(env.getProperty("service.jwt.secret")).thenReturn("d://private_key.der")
 * ; JwtToken t = jwtServiceImpl.createLoginToken(user);
 * when(env.getProperty("service.jwt.public")).thenReturn("d://public_key.der");
 * Claims Claims= jwtServiceImpl.validate(t); assertNotNull(Claims);
 * 
 * }
 * 
 * }
 */