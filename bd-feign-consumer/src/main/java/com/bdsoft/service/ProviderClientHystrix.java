package com.bdsoft.service;

import org.springframework.stereotype.Component;

/**
 * Feign，服务提供者异常处理
 */
@Component
public class ProviderClientHystrix implements ProviderClient {

	@Override
	public String append(String a, String b) {
		return "provider error!";
	}

}
