package com.bdsoft;

import feign.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Bean;

// 开启断路器
@EnableCircuitBreaker
// 用于启用feign客户端，类似rpc方式调用服务提供者
@EnableFeignClients
// 用于发现服务
@EnableDiscoveryClient
@SpringBootApplication
public class FeignConsumerApplication {

    // feign日志级别配置
    @Bean
    Logger.Level feignLoggerLevel(){
        return Logger.Level.FULL;
    }

    public static void main(String[] args) {
        SpringApplication.run(FeignConsumerApplication.class, args);
    }
}

