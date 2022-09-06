package com.oven.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @Value("${com.name}")
    private String name;

    @RequestMapping("/get")
    public String get() {
        return name;
    }

}
