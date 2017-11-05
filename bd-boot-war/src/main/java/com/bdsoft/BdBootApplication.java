package com.bdsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.bdsoft.*")
public class BdBootApplication {

    public static void main(String[] args) {
        // default bootstrap
//        SpringApplication.run(BdBootApplication.class, args);

        // add special configs for bootstrap
        SpringApplication app = new SpringApplication(BdBootApplication.class);
        // TODO config others
        app.run(args);
    }

}
