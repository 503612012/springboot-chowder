package com.oven.controller;

import com.oven.config.MemcachedRunner;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private MemcachedRunner memcachedRunner;

    @RequestMapping("/set")
    public void set(String key, String value) {
        memcachedRunner.getClient().set(key, 1000, value);
    }

    @RequestMapping("/get")
    public String get(String key) {
        return memcachedRunner.getClient().get(key).toString();
    }

}
