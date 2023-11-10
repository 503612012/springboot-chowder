package com.oven.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {

    @GetMapping("/hi")
    public String hi(String name) {
        return "hi , " + name;
    }

    @GetMapping("/hello")
    public String hello(String name) {
        return "hello , " + name;
    }

}
