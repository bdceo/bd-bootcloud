package com.bdsoft;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * 发现服务客户端，需要被注册的服务，作为服务提供者
 */
@EnableDiscoveryClient
@SpringBootApplication
public class EurekaProviderApplication {

	public static void main(String[] args) {
		new SpringApplicationBuilder(EurekaProviderApplication.class).web(true).run(args);
		// SpringApplication.run(ProviderApplication.class, args);
	}
}
