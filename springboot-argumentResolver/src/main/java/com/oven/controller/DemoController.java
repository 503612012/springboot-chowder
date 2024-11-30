package com.oven.controller;

import com.oven.annotation.CurrentUser;
import com.oven.entity.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public Object test(@CurrentUser User user) {
        return user;
    }

}
