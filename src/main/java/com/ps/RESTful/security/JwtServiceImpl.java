package com.ps.RESTful.security;

import static java.util.Objects.requireNonNull;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jboss.logging.Logger;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;

import com.ps.RESTful.enums.ErrorCode;
import com.ps.RESTful.error.handler.InvalidRequestException;
import com.ps.entities.master.GlobalUserMaster;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Service
public final class JwtServiceImpl implements JwtService {

	Logger logger = Logger.getLogger(getClass());

	private final Environment env;

	private Claims claims;

	public JwtServiceImpl(Environment env) {
		this.env = requireNonNull(env);
	}

	// Return-Claims-Used-in-RequestUtils
	@Override
	public Claims getClaims() {
		return claims;
	}

	@Override
	public JwtToken createLoginToken(GlobalUserMaster user) {

		return createJWT(UUID.randomUUID().toString(), env.getProperty("service.jwt.issuer"),
				String.valueOf(user.getUserMasterId()), user,
				Long.parseLong(env.getProperty("service.jwt.expiration")));
	}

	@Override
	public Claims validate(JwtToken token) {
		try {

			final Key signingKey = EncryptionUtil.getPublicKey(env.getProperty("service.jwt.public"));

			// Removing-Bearer-Keyword-from-Token-as-it-is-invalid-while-parsing
			JwtHeaderTokenExtractor extractedToken = new JwtHeaderTokenExtractor();
			String jwtToken = extractedToken.extract(token.getToken());

			// Parsing-JSON-Token-and-Decoding-it-by-key
			claims = Jwts.parser().setSigningKey(signingKey).parseClaimsJws(jwtToken).getBody();

			return claims;

		} catch (ExpiredJwtException ex) {
			if (logger.isDebugEnabled())
				logger.debug("Token has expired:" + ex.getMessage());

			throw ex;
		} catch (Exception e) {

			if (logger.isDebugEnabled())
				logger.debug("Failed to parse Token:" + e.getMessage());

			throw new InvalidRequestException(ErrorCode.INTERNAL_SERVER_ERROR, "Failed to parse Token");
		}

	}

	private JwtToken createJWT(String id, String issuer, String subject, GlobalUserMaster userDetails,
			long ttlMillis) {

		// The JWT signature algorithm we will be using to sign the token
		final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.RS256;

		final long nowMillis = System.currentTimeMillis();
		final Date now = new Date(nowMillis);

		// We will sign our JWT with our ApiKey secret
		final Key signingKey = EncryptionUtil.getPrivateKey(env.getProperty("service.jwt.secret"));
		
		// Hold-LoginInfo
		HashMap<String, Object> loginInfo = new HashMap<String, Object>();

		loginInfo.put("userName", userDetails.getUserName());
		loginInfo.put("emailId", userDetails.getEmailId());
		loginInfo.put("groupName", userDetails.getGroupDBMaster().getGroupName());
		loginInfo.put("companyId", userDetails.getGlobalCompanyMaster().getGlobalCompanyMasterId());
		loginInfo.put("companyName", userDetails.getGlobalCompanyMaster().getCompanyName());
		loginInfo.put("employeeMasterId", userDetails.getEmployeeMasterId());

		// Adding-into-JWT-token
		final Map<String, Object> claims = new HashMap<>();
		claims.put("UserDetails", loginInfo);

		// Let's set the JWT Claims
		final JwtBuilder builder = Jwts.builder().setClaims(claims).setId(id).setIssuer(issuer).setIssuedAt(now)
				.setSubject(subject).signWith(signatureAlgorithm, signingKey);

		// If it has been specified, let's add the expiration
		if (ttlMillis >= 0) {
			long expMillis = nowMillis + ttlMillis;
			Date exp = new Date(expMillis);
			builder.setExpiration(exp);
		}

		// Builds the JWT and serializes it to a compact, URL-safe string
		return new JwtToken(builder.compact());
	}
}