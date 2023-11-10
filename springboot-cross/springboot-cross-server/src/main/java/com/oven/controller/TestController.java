package com.oven.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/test")
    @CrossOrigin(value = {"http://localhost:8081"})
    public Object test1() {
        return "test1";
    }

    @PostMapping("/test")
    @CrossOrigin(value = {"http://localhost:8081"})
    public Object test2() {
        return "test2";
    }

}
