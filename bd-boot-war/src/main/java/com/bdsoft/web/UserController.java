package com.bdsoft.web;

import com.bdsoft.entity.BdUser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/{id}")
    public String getUser(@PathVariable("id") Long id) {
        log.info("get user id={}", id);
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
