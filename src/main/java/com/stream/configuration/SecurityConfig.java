package com.stream.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.stream.domain.role.RolesEnum;

@Configuration
public class SecurityConfig {
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		// JWT 인증이기 때문에 csrf 토큰 인증은 의미 중복
		http.csrf(csrf -> csrf.disable())
			.httpBasic(c -> c.disable())
			.authorizeHttpRequests(c -> {
				c.requestMatchers("/login", "/sign-up").permitAll()
					.anyRequest()
					.hasAnyAuthority(RolesEnum.USER.name(), RolesEnum.UPLOADER.name(), RolesEnum.ADMIN.name());
			});
		return http.build();
	}
}
