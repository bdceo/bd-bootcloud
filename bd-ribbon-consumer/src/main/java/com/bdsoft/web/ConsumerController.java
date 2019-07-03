package com.bdsoft.web;

import com.bdsoft.service.ProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class ConsumerController {

    @Autowired
    RestTemplate template;
    @Autowired
    ProviderService service;

    @Autowired
    private LoadBalancerClient client;

    public static String PROVIDER_SERVICE_ID = "bd-provider-1";

    @RequestMapping(value = "/append1", method = RequestMethod.GET)
    public String appendByTemplate() {
        return template.getForEntity("unirest://" + PROVIDER_SERVICE_ID + "/v1/provider/append?a=from&b=To", String.class).getBody();
    }

    @RequestMapping(value = "/append2", method = RequestMethod.GET)
    public String appendWithError() {
        return service.append();
    }

    @RequestMapping(value = "/append3", method = RequestMethod.GET)
    public String appendByClient() {
        ServiceInstance remoteService = client.choose(PROVIDER_SERVICE_ID);
        StringBuilder sb = new StringBuilder();
        sb.append("serviceId=").append(remoteService.getServiceId());
        sb.append(", host=").append(remoteService.getHost());
        sb.append(", port=").append(remoteService.getPort());
        return sb.toString();
    }

}
