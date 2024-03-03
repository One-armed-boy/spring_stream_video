package com.stream.configuration;

import java.lang.reflect.Type;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

@Configuration
public class HttpMsgConverterConfig {
	@Bean
	public AbstractJackson2HttpMessageConverter abstractJackson2HttpMessageConverter(ObjectMapper objectMapper) {
		return new MultipartJackson2HttpMessageConverter(objectMapper);
	}

	private class MultipartJackson2HttpMessageConverter extends AbstractJackson2HttpMessageConverter {

		/**
		 * "Content-Type: multipart/form-data" 헤더를 지원하는 HTTP 요청 변환기
		 */
		public MultipartJackson2HttpMessageConverter(ObjectMapper objectMapper) {
			super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
		}

		@Override
		public boolean canWrite(Class<?> clazz, MediaType mediaType) {
			return false;
		}

		@Override
		public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
			return false;
		}

		@Override
		protected boolean canWrite(MediaType mediaType) {
			return false;
		}
	}
}
