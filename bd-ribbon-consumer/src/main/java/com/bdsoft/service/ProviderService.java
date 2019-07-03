package com.bdsoft.service;

import com.bdsoft.web.ConsumerController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheKey;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheRemove;
import com.netflix.hystrix.contrib.javanica.cache.annotation.CacheResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class ProviderService {

    private Logger log = LoggerFactory.getLogger(getClass());
    @Autowired
    RestTemplate template;

    @HystrixCommand(fallbackMethod = "onProviderError", commandKey = "appendKey",
            commandProperties = {
                    @HystrixProperty(name = "execution.isolation.thread.timeoutInMilliseconds", value = "2000"),
                    @HystrixProperty(name = "metrics.rollingStats.timeInMilliseconds", value = "5000")
            },
            // observableExecutionMode = ObservableExecutionMode.LAZY,
            threadPoolProperties = {
                    @HystrixProperty(name = "coreSize", value = "1"),
                    @HystrixProperty(name = "maxQueueSize", value = "10")
            })
    public String append() {
        Long start = System.currentTimeMillis();

        String res = template.getForEntity("unirest://" + ConsumerController.PROVIDER_SERVICE_ID + "/v1/provider/append?a=To&b=From", String.class).getBody();

        log.info("Spend time: {}", (System.currentTimeMillis() - start));
        return res;
    }


    @CacheResult(cacheKeyMethod = "showCacheKey") // 开启请求缓存
    @HystrixCommand(commandKey = "show")
    public String show(@CacheKey("id") Long id) {
        return template.getForEntity("unirest://" + ConsumerController.PROVIDER_SERVICE_ID + "/v1/provider/append?a=To&b=From", String.class).getBody();
    }

    // 清除缓存
    @CacheRemove(commandKey = "show")
    @HystrixCommand
    public void rm(@CacheKey("id") Long id) {
        // TODO delete object
    }

    // 生成缓存key方法，优先级高于cacheKey
    public Long showCacheKey(Long id) {
        return id;
    }

    // 服务提供者错误时回调函数
    public String onProviderError() {
        return "provider error!";
    }

}
