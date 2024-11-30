package com.oven.controller;

import com.oven.service.TaskMessageService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class TestController {

    @Resource
    private TaskMessageService taskMessageService;

    @RequestMapping("/test")
    public Object test(String msg) {
        taskMessageService.sendMessage(msg);
        return "发送成功";
    }

}
