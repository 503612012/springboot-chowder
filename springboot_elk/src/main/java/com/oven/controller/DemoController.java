package com.oven.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class DemoController {

    @RequestMapping("/test")
    public String test(String key) {
        log.info("打印日志了。。。{}", key);
        return "打印日志成功";
    }

}
