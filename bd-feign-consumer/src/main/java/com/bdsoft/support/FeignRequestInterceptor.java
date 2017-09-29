package com.bdsoft.support;

import javax.servlet.http.HttpServletRequest;

import com.bdsoft.support.WebHandlerInterceptor;
import feign.Request;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import feign.RequestInterceptor;
import feign.RequestTemplate;

@Component
public class FeignRequestInterceptor implements RequestInterceptor {

	@Autowired
	private WebHandlerInterceptor interceptor;

	@Override
	public void apply(RequestTemplate template) {
		// can't got spring-request context
		RequestAttributes reqAtt = RequestContextHolder.getRequestAttributes();

		try {
			String header = interceptor.getBdHeader();
			String cookie = interceptor.getBdCookie();
			StringBuilder auth = new StringBuilder().append(header).append(cookie);
			template.header("Authorization", auth.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}

