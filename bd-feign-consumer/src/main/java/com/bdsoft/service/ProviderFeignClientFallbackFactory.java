package com.bdsoft.service;

import feign.hystrix.FallbackFactory;
import org.springframework.stereotype.Component;

/**
 * provider的服务存根，同时指定服务提供者错误处理
 */
@Component
public class ProviderFeignClientFallbackFactory implements FallbackFactory<ProviderFeignClient> {

    @Override
    public ProviderFeignClient create(Throwable cause) {
        return new ProviderFeignClient() {
            @Override
            public String append(String a, String b) {
                if(cause instanceof RuntimeException){
                    // TODO 特殊异常处理
                }
                return "append fallback!";
            }

            @Override
            public String time() {
                return String.valueOf(System.currentTimeMillis());
            }
        };
    }
}
