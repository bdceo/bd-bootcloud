package com.bdsoft.service;

import com.bdsoft.pojo.User;
import com.netflix.hystrix.HystrixCommand;

import java.util.List;

import static com.netflix.hystrix.HystrixCommandGroupKey.Factory.asKey;

/**
 * 功能
 *
 * @author 丁辰叶
 * @version 1.0
 * @date 2018/5/21 10:51
 */
public class UserBatchCommand extends HystrixCommand<List<User>> {

    private UserService userService;
    private List<Long> userIds;

    public UserBatchCommand(UserService userService, List<Long> userIds) {
        super(Setter.withGroupKey(asKey("userServiceCommand")));
        this.userIds = userIds;
        this.userService = userService;
    }

    @Override
    protected List<User> run() throws Exception {
        return userService.findAll(userIds);
    }
}
