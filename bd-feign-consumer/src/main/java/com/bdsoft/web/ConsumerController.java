package com.bdsoft.web;

import com.bdsoft.service.ProviderClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;

import javax.servlet.http.HttpServletRequest;
import java.util.Random;

@RestController
public class ConsumerController {

    @Autowired
    ProviderClient client;

    @RequestMapping(value = "/append", method = RequestMethod.GET)
    public String append(HttpServletRequest request) {
        RequestContextHolder.currentRequestAttributes();
        return client.append("xx" + new Random().nextInt(100), "oo" + new Random().nextInt(100));
    }

    @RequestMapping(value = "/time", method = RequestMethod.GET)
    public String time() {
        return client.time();
    }
}
