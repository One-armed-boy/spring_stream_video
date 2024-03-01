package com.stream.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityContextHelper {
	public String getMember() {
		return SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
	}
}
