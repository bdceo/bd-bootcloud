package com.bdsoft.feign;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfiguration {

	@Bean
	public FeignRequestInterceptor basicAuthRequestInterceptor() {
		return new FeignRequestInterceptor();
	}

}
