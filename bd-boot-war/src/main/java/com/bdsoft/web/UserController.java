package com.bdsoft.web;

//import org.apache.logging.log4j.LogManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.bdsoft.entity.BdUser;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger slf4j = LoggerFactory.getLogger(UserController.class);
//    private org.apache.logging.log4j.Logger log4j2 = LogManager.getLogger(UserController.class);

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id) {
        slf4j.info("get user id={}", id);
//        log4j2.info("get user id={}", id);

        try {
            int r = 1 / 0;
        } catch (Exception e) {
            slf4j.error("slf4j-error:", e);
//            log4j2.error("log4j2-error:", e);
            e.printStackTrace();
        }

        return "hello user#" + id;
    }

    @PostMapping("/")
    public String addUser(BdUser user) {
        slf4j.info("add user, id={}, name={}", user.getId(), user.getName());
        return "add user success!";
    }

    @PutMapping("/{id}")
    public String updateUser(@PathVariable("id") Long id, @RequestParam String name) {
        slf4j.info("update user id={}, name={}", id, name);
        return "update user#" + id + " success!";
    }

    @DeleteMapping("/{id}")
    public String deleteUser(@PathVariable("id") Long id) {
        slf4j.info("delte user id={}", id);
        return "delete user#" + id + " success!";
    }

}
