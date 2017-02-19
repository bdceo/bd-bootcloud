package com.bdsoft.feign;

import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRequestInterceptor implements RequestInterceptor {

	public FeignRequestInterceptor() {
	}

	@Override
	public void apply(RequestTemplate template) {
		
		
		template.header("Authorization", "bdceo");
	}

}
