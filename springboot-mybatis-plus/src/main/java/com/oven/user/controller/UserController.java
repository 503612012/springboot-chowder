package com.oven.user.controller;

import com.oven.user.service.IUserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private IUserService userService;

    @RequestMapping("/test")
    public void test() {
        userService.test();
    }

}
