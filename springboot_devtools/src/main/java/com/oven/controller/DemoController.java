package com.oven.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public String test(String name) {
        return "hello " + name;
    }

}
