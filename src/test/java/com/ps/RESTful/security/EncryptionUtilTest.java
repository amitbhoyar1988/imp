package com.ps.RESTful.security;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.boot.test.context.SpringBootTest;

@RunWith(MockitoJUnitRunner.class)
@SpringBootTest
public class EncryptionUtilTest {
	
	@InjectMocks
	EncryptionUtil encryptionUtil;
	
	@Test
	public void getPublicKeyTest() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		
		final byte[] keyBytes = Files.readAllBytes(Paths.get("d://public_key.der"));
        final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
        PublicKey expected =  KeyFactory.getInstance("RSA").generatePublic(spec);
        PublicKey actual = encryptionUtil.getPublicKey("d://public_key.der");
        assertEquals(expected, actual);
        
	}


	@Test
	public void getPrivateKey() throws InvalidKeySpecException, NoSuchAlgorithmException, IOException {
		
		final byte[] keyBytes = Files.readAllBytes(Paths.get("d://private_key.der"));
        final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        PrivateKey expected =  KeyFactory.getInstance("RSA").generatePrivate(spec);
        PrivateKey actual = encryptionUtil.getPrivateKey("d://private_key.der");
        assertEquals(expected, actual);
        
	}
}
