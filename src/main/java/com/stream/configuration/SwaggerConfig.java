package com.stream.configuration;

import org.springframework.context.annotation.Configuration;

import com.stream.security.jwt.JwtMetadata;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.security.SecuritySchemes;

@OpenAPIDefinition(
	info = @Info(title = "Spring Stream API Swagger", description = "API 명세를 위한 Swagger 페이지")
)
@SecuritySchemes(
	value = {
		@SecurityScheme(type = SecuritySchemeType.APIKEY, in = SecuritySchemeIn.COOKIE, name = JwtMetadata.ACCESS_TOKEN_KEY)
	}
)
@Configuration
public class SwaggerConfig {
}
