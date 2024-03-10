package com.stream.configuration;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.stream.msgconverter.OctetStreamReadMsgConverter;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private OctetStreamReadMsgConverter octetStreamReadMsgConverter;

	@Autowired
	public WebConfig(OctetStreamReadMsgConverter octetStreamReadMsgConverter) {
		this.octetStreamReadMsgConverter = octetStreamReadMsgConverter;
	}

	@Override
	public void configureMessageConverters(List<HttpMessageConverter<?>> converters) {
		converters.add(octetStreamReadMsgConverter);
	}
}