package com.oven.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Value("${server.port}")
    private String port;

    @RequestMapping("/test")
    public String test() {
        return "hello " + port;
    }

}
