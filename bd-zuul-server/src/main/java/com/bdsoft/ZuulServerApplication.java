package com.bdsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.cloud.client.SpringCloudApplication;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;
import org.springframework.context.annotation.Bean;

import com.bdsoft.web.AccessFilter;

// 开启zuul网管代理
@EnableZuulProxy
// 集成了：SpringBootApplication，EnableDiscoryClient，EnableCiruitBreaker
@SpringCloudApplication
public class ZuulServerApplication {

	// 注册Zuul-Filter
	@Bean
	public AccessFilter accessFilter() {
		return new AccessFilter();
	}

	public static void main(String[] args) {
		SpringApplication.run(ZuulServerApplication.class, args);
	}
}
