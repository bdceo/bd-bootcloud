package com.bdsoft.service;

import com.bdsoft.web.ConsumerController;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
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

        String res = template.getForEntity("http://" + ConsumerController.PROVIDER_SERVICE_ID + "/v1/provider/append?a=To&b=From", String.class).getBody();

        log.info("Spend time: {}", (System.currentTimeMillis() - start));
        return res;
    }

    // 服务提供者错误时回调函数
    public String onProviderError() {
        return "provider error!";
    }

}
