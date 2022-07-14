package com.ps.RESTful.security;

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

import org.jboss.logging.Logger;

public final class EncryptionUtil {
	static Logger logger = Logger.getLogger(EncryptionUtil.class);

	public static PublicKey getPublicKey(String filename) {
		try {
			final byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
			final X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);

			return factory().generatePublic(spec);
		} catch (IOException ex) {
			logger.error("Error loading public key" + ex.getMessage());
			throw new RuntimeException("Error loading public key '" + filename + "'.", ex);
		} catch (final InvalidKeySpecException ex) {
			logger.error("Error reading public key " + ex.getMessage());

			throw new RuntimeException("Error reading public key '" + filename + "'.", ex);
		}
	}

	public static PrivateKey getPrivateKey(String filename) {
		try {
			final byte[] keyBytes = Files.readAllBytes(Paths.get(filename));
			final PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
			return factory().generatePrivate(spec);
		} catch (IOException ex) {

			logger.error("Error loading private key " + ex);

			throw new RuntimeException("Error reading private key  '" + filename + "'.", ex);
		} catch (final InvalidKeySpecException ex) {
			logger.error("Error loading private key " + ex);

			throw new RuntimeException("Error reading private key '" + filename + "'.", ex);
		}
	}

	private static KeyFactory factory() {
		try {
			return KeyFactory.getInstance("RSA");
		} catch (final NoSuchAlgorithmException ex) {
			logger.error("Could not find the RSA algorithm." + ex);

			throw new RuntimeException("Could not find the RSA algorithm.", ex);
		}
	}

	private EncryptionUtil() {
	}
}