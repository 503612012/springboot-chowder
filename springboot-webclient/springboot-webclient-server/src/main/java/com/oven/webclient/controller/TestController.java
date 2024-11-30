package com.oven.webclient.controller;

import com.oven.webclient.service.TestService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private TestService testService;

    @RequestMapping("/test")
    public Object test() {
        return testService.test();
    }

}
