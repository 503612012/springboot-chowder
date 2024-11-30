package com.oven.controller;

import com.oven.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public User test(User user) {
        user.setAge(18);
        return user;
    }

}
