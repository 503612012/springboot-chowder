package com.oven.controller;

import com.oven.service.AsyncService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private AsyncService asyncService;

    @RequestMapping("/test")
    public String test() {
        try {
            asyncService.doSomething();
            return "success";
        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

}
