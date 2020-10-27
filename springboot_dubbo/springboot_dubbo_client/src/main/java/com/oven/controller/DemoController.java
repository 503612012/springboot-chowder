package com.oven.controller;

import com.oven.service.DemoService;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @Reference(version = "1.0.0")
    private DemoService demoService;

    @RequestMapping("/test")
    public String test(String name) {
        return demoService.sayHello(name);
    }

}
