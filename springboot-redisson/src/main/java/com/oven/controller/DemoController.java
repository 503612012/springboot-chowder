package com.oven.controller;

import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

@RestController
public class DemoController {

    @Resource
    private RedissonClient redissonClient;

    @RequestMapping("lock")
    public String lock() {
        try {
            RLock lock = redissonClient.getLock("lock");
            lock.lock(60, TimeUnit.SECONDS);
            System.out.println("do something...");
            lock.unlock();
            return "成功";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
