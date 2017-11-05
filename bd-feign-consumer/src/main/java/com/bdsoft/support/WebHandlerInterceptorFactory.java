package com.bdsoft.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * 功能
 *
 * @version 1.0
 * @auth 丁辰叶
 * @date 2017/9/27 15:25
 */
@Configuration
public class WebHandlerInterceptorFactory extends WebMvcConfigurerAdapter {

    @Autowired
    private WebHandlerInterceptor interceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(interceptor);
    }
}
