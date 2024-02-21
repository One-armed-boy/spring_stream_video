package com.stream.security.jwt;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.stream.domain.role.Role;
import com.stream.domain.role.RolesEnum;

@Component
public class JwtManager {
	private static final int ACCESS_TOKEN_EXPIRE_MS = 5 * 60 * 1000;
	@Value("${my.app.jwt.signingKey}")
	private String signingKey;

	public String issueToken(DecodedJwtAccessToken decodedToken) {
		var email = decodedToken.getEmail();
		var role = decodedToken.getRole();
		return JWT.create()
			.withExpiresAt(new Date(System.currentTimeMillis() + ACCESS_TOKEN_EXPIRE_MS))
			.withClaim("email", email)
			.withClaim("role", role.getName().name())
			.sign(getAlgorithm());
	}

	private Algorithm getAlgorithm() {
		return Algorithm.HMAC512(signingKey);
	}

	public DecodedJwtAccessToken decodeToken(String token) {
		DecodedJWT decoded = JWT.require(getAlgorithm()).build().verify(token);
		var email = decoded.getClaim("email").asString();
		var role = new Role(RolesEnum.valueOf(decoded.getClaim("role").asString()));
		return new DecodedJwtAccessToken(email, role);
	}
}
