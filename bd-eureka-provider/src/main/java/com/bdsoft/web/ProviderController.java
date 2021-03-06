package com.bdsoft.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Enumeration;
import java.util.Random;

@RestController
@RequestMapping("/v1/provider")
public class ProviderController {

    private Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private DiscoveryClient client;

    @RequestMapping("/append")
    public String append(HttpServletRequest request, String a, String b) throws Exception {
        String auth = request.getHeader("Authorization");
        log.info("unirest-header={}", auth);

        String res = a + b;

        ServiceInstance instance = client.getInstances("BD-PROVIDER-1").get(0);
        log.info("/append, host={}, service_id={}, result={}", instance.getHost(), instance.getServiceId(), res);

        // 模拟响应慢
        int sleep = new Random().nextInt(3000);
        log.info("sleepTime:{}", sleep);
        Thread.sleep(sleep);

        return res;
    }

    @RequestMapping("/time")
    public String time() {
        return new Date().toInstant().toString();
    }

}
