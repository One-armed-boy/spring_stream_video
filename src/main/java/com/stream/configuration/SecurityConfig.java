package com.stream.configuration;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.stream.domain.role.RolesEnum;
import com.stream.security.jwt.JwtAuthFailFilter;
import com.stream.security.jwt.JwtAuthenticationFilter;
import com.stream.swagger.SwaggerMetadata;

@Configuration
public class SecurityConfig {
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtAuthFailFilter jwtAuthFailFilter;

	@Autowired
	public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter, JwtAuthFailFilter jwtAuthFailFilter) {
		this.jwtAuthenticationFilter = jwtAuthenticationFilter;
		this.jwtAuthFailFilter = jwtAuthFailFilter;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// JWT 인증이기 때문에 csrf 토큰 인증은 의미 중복
		http.csrf(csrf -> csrf.disable())
			.httpBasic(c -> c.disable())
			.addFilterAfter(jwtAuthenticationFilter, BasicAuthenticationFilter.class)
			.addFilterBefore(jwtAuthFailFilter, JwtAuthenticationFilter.class)
			.authorizeHttpRequests(c -> {
				c.requestMatchers(getRequestMatchersForPermitAll()).permitAll()
					.anyRequest()
					.hasAnyAuthority(RolesEnum.USER.name(), RolesEnum.UPLOADER.name(), RolesEnum.ADMIN.name());
			});
		return http.build();
	}

	private String[] getRequestMatchersForPermitAll() {
		List<String> resultList = new ArrayList<>();
		// for swagger
		resultList.addAll(
			SwaggerMetadata.getSwaggerPathPrefixes().stream().map(pathPrefix -> pathPrefix + "/**").toList());

		resultList.addAll(List.of("/login", "/sign-up"));
		return resultList.toArray(String[]::new);
	}
}
