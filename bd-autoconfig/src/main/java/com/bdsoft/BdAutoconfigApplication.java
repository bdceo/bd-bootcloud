package com.bdsoft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BdAutoconfigApplication {

    public static void main(String[] args) {
        SpringApplication.run(BdAutoconfigApplication.class, args);
    }


    /**
     * 【应用配置类】
     * http://localhost:8080/autoconfig     自动化配置报告：positiveMatches-匹配成功，negativeMatches-匹配不成功
     *
     * http://localhost:8080/beans  应用上下文创建的所有Bean
     *
     * http://localhost:8080/configprops    应用中配置的属性信息报告
     *
     * http://localhost:8080/env    应用所有可用的环境属性报告
     *
     * http://localhost:8080/mappings   SpringNVC控制器映射关系报告
     *
     * http://localhost:8080/info   应用自定义信息
     *
     * 【度量指标类】
     * http://localhost:8080/metrics    各类重要度量指标
     *
     * http://localhost:8080/health     各类健康指标信息
     *
     * http://localhost:8080/dump       线程信息
     *
     * http://localhost:8080/trace      基本的HTTP跟踪信息，最近100条
     *
     * 【操作控制类】
     * /shutdown    停服
     *
     */
}
