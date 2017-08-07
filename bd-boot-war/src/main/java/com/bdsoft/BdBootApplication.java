package com.bdsoft;

import com.bdsoft.config.CustomConfig;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bdsoft.*")
public class BdBootApplication  {

    public static void main(String[] args) {
        SpringApplication.run(BdBootApplication.class, args);
    }

}
