package com.bdsoft.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

@Service
public class ProviderService {

	@Autowired
	RestTemplate template;

	@HystrixCommand(fallbackMethod = "onProviderError")
	public String append() {
		return template.getForEntity("http://BD-PROVIDER/append?a=xx&b=oo", String.class).getBody();
	}

	// 服务提供者错误时回调函数
	public String onProviderError() {
		return "provider error!";
	}

}
