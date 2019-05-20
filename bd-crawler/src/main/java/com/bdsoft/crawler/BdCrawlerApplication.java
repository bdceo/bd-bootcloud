package com.bdsoft.crawler;

import org.mybatis.spring.annotation.MapperScan;
import org.mybatis.spring.annotation.MapperScans;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.bdsoft.crawler.modules.*.mapper*")
public class BdCrawlerApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdCrawlerApplication.class, args);
    }

}
