package com.bdsoft.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ProviderController {

	private Logger log = LoggerFactory.getLogger(getClass());

	@Autowired
	private DiscoveryClient client;

	@RequestMapping("/append")
	public String append(String a, String b) {

		String res = a + b;

		ServiceInstance instance = client.getLocalServiceInstance();
		log.info("/append, host={}, service_id={}, result={}", instance.getHost(), instance.getServiceId(), res);

		return res;
	}

}
