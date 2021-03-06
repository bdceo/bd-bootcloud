package com.bdsoft.service;

import feign.Headers;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * provider的服务存根，同时指定服务提供者错误处理
 */
@FeignClient(value = "bd-provider-1",path="/v1/provider", fallback = ProviderClientHystrix.class)
public interface ProviderClient {

//	@Headers("Authorization")
	@RequestMapping(value = "/append", method = RequestMethod.GET)
	String append(@RequestParam("a") String a, @RequestParam("b") String b);

	@RequestMapping(value = "/time", method = RequestMethod.GET)
	String time();

}
