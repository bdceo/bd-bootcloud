package com.bdsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;

import com.bdsoft.feign.FeignConfiguration;

// 用于启用feign客户端，类似rpc方式调用服务提供者
@EnableFeignClients(defaultConfiguration = FeignConfiguration.class)
// 用于发现服务
@EnableDiscoveryClient
@SpringBootApplication
public class FeignConsumerApplication {
	
	public static void main(String[] args) {
		SpringApplication.run(FeignConsumerApplication.class, args);
	}
}
