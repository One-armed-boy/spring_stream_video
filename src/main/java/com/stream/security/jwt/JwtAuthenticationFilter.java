package com.stream.security.jwt;

import java.io.IOException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

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
		// TODO: 액세스 토큰을 쿠키에서 추출하도록 수정
		DecodedJwtAccessToken decodedToken = jwtManager.decodeToken(request.getHeader("Authorization"));

		String email = decodedToken.getEmail();
		String roleName = decodedToken.getRole().getName().name();
		GrantedAuthority authority = new SimpleGrantedAuthority(roleName);

		Authentication auth = new UsernamePasswordAuthenticationToken(email, null,
			List.of(authority));

		SecurityContextHolder.getContext().setAuthentication(auth);

		filterChain.doFilter(request, response);
	}

	@Override
	protected boolean shouldNotFilter(HttpServletRequest request) {
		String path = request.getServletPath();
		return path.equals("/login") || path.equals("/sign-up");
	}
}
