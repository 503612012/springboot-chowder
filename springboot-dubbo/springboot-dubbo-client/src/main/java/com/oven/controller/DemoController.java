package com.oven.controller;

import com.oven.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Reference(version = "1.0.0")
    private DemoService demoService;

    @GetMapping("/test")
    public String getUserById(String name) {
        return demoService.sayHello(name);
    }

}
