package com.bdsoft.web;

import com.bdsoft.config.CustomConfig;
import com.bdsoft.config.InfoConfig;
import com.bdsoft.entity.BdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.web.bind.annotation.*;

import javax.servlet.AsyncContext;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private InfoConfig infoConfig;
    @Autowired
    private CustomConfig customConfig;
    @Autowired
    private Environment env;


    /**
     * Servlet 3.0 api
     * @param req
     * @return
     */
    @PostMapping("/xx")
    public String addXx(HttpServletRequest req) {
        // 初始化异步处理
        AsyncContext asyCtx = req.startAsync();

        // 开始异步处理
        asyCtx.start(()->{
            // do sth need long time
            // 输出
            asyCtx.getResponse();
            // 结束通知
            asyCtx.complete();
        });

        return "add user success!";
    }


    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id) {
        log.info("get user id={}", id);

        log.info("env-info.app.name={}", env.getProperty("info.app.name"));

        log.info("default-configs:{}", infoConfig);

        log.info("custom-configs:{}", customConfig);

        return "hello user#" + id;
    }

    @PostMapping("/")
    public String addUser(BdUser user) {
        log.info("add user, id={}, name={}", user.getId(), user.getName());
        return "add user success!";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @RequestParam String name) {
        log.info("update user id={}, name={}", id, name);
        return "update user#" + id + " success!";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        log.info("delte user id={}", id);
        return "delete user#" + id + " success!";
    }

}
