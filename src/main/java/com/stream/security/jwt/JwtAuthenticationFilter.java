package com.stream.security.jwt;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.stream.exception.BaseException;
import com.stream.security.jwt.exception.AccessTokenNotFoundException;
import com.stream.security.jwt.exception.JwtAuthWrapperException;
import com.stream.swagger.SwaggerMetadata;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
	private final JwtManager jwtManager;

	@Autowired
	public JwtAuthenticationFilter(JwtManager jwtManager) {
		this.jwtManager = jwtManager;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
		FilterChain filterChain) throws ServletException, IOException {
		try {
			DecodedJwtAccessToken decodedToken = jwtManager.decodeToken(extractAccessToken(request));
			String email = decodedToken.getEmail();
			String roleName = decodedToken.getRole().getName().name();
			GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

			Authentication auth = new UsernamePasswordAuthenticationToken(email, null,
				List.of(authority));

			SecurityContextHolder.getContext().setAuthentication(auth);

			logger.debug(
				"Jwt Auth Success (path: " + request.getServletPath() + " token: " + decodedToken + " )");
		} catch (BaseException err) {
			throw new JwtAuthWrapperException(err);
		}

		filterChain.doFilter(request, response);
	}

	private String extractAccessToken(HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();
		if (cookies == null) {
			throw new AccessTokenNotFoundException();
		}
		Cookie accessTokenCookie = Arrays.stream(cookies)
			.filter(cookie -> cookie.getName().equals(JwtMetadata.ACCESS_TOKEN_KEY))
			.findFirst()
			.orElseThrow(AccessTokenNotFoundException::new);
		return accessTokenCookie.getValue();
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getRequestURI();
		return path.equals("/login") || path.equals("/sign-up")
			|| SwaggerMetadata.getSwaggerPathPrefixes().stream()
			.anyMatch(path::startsWith);
	}
}
