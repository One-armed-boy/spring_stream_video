package com.stream.swagger;

import java.util.List;

public class SwaggerMetadata {
	public static List<String> getSwaggerPathPrefixes() {
		return List.of("/swagger-ui", "/v3/api-docs");
	}
}
