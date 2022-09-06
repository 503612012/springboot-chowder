package com.oven.controller;

import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public User test() {
        User user = new User();
        user.setId(27);
        user.setName("Oven");
        user.setCreateTime(new Date());
        return user;
    }

}
