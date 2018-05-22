package com.bdsoft.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RefreshScope
@RestController
public class TestController {

    @Value("${server}")
    private String server;

    @Autowired
    private Environment env;

    @RequestMapping("/server")
    public String server() {
        return getServer();
    }

    @RequestMapping("/server/env")
    public String serverOfEnv() {
        return env.getProperty("server", "unKnown");
    }

    public String getServer() {
        return server;
    }

    public void setServer(String server) {
        this.server = server;
    }
}
