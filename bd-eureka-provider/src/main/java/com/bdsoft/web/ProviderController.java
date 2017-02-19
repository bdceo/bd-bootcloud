package com.bdsoft.web;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/provider")
public class ProviderController {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DiscoveryClient client;

	@RequestMapping("/append")
	public String append(HttpServletRequest request, String a, String b) {

		String auth = request.getHeader("Authorization");
		log.info("http-header={}", auth);
		
		String res = a + b;

		ServiceInstance instance = client.getLocalServiceInstance();
		log.info("/append, host={}, service_id={}, result={}", instance.getHost(), instance.getServiceId(), res);

		return res;
	}

}
