package com.oven.controller;

import com.oven.vo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public String index(String name) {
        return "hello " + name;
    }

    @GetMapping("/get")
    public User get(User user) {
        System.out.println("get 入参:[" + user.toString() + "]");
        user.setUname("hello get " + user.getUname());
        return user;
    }

    @PostMapping("/post")
    public User post(User user) {
        System.out.println("post 入参:[" + user.toString() + "]");
        user.setUname("hello post " + user.getUname());
        return user;
    }

}