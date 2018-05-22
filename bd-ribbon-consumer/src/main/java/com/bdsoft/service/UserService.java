package com.bdsoft.service;

import com.bdsoft.pojo.User;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCollapser;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixProperty;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

/**
 * 功能
 *
 * @author 丁辰叶
 * @version 1.0
 * @date 2018/5/21 10:45
 */
@Service
public class UserService {

    @Autowired
    private RestTemplate rest;

    // 单个查询
    public User find(Long id) {
        return rest.getForObject("http://User-Service/users/{1}", User.class, id);
    }

    // 批量查询
    public List<User> findAll(List<Long> ids) {
        return rest.getForObject("http://user-service/users?ids={1}", List.class, StringUtils.join(ids, ","));
    }

    // 注解方式
    @HystrixCollapser(batchMethod = "findAll2", collapserProperties = {
            @HystrixProperty(name = "timerDelayInMilliseconds", value = "1000")
    })
    public User find2(Long id) {
        return rest.getForObject("http://User-Service/users/{1}", User.class, id);
    }

    @HystrixCommand
    public List<User> findAll2(List<Long> ids) {
        return rest.getForObject("http://user-service/users?ids={1}", List.class, StringUtils.join(ids, ","));
    }


}
