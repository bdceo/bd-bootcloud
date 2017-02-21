package com.bdsoft.feign;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

public class FeignRequestInterceptor implements RequestInterceptor {

	public FeignRequestInterceptor() {
	}

	@Override
	public void apply(RequestTemplate template) {
		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
				.getRequest();
		String dd = request.getParameter("dd");

		template.header("Authorization", "bdceo");
	}

}
