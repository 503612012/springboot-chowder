package com.oven.controller;

import com.oven.entity.User;
import com.oven.service.RedisService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Value("${app.info.author}")
    private String author;

    @Resource
    private RedisService redisService;

    @RequestMapping("/redis")
    public String redis() {
        System.out.println((String) redisService.get("name"));
        redisService.set("name", "Oven");
        System.out.println((String) redisService.get("name"));
        redisService.remove("name");
        System.out.println((String) redisService.get("name"));

        System.out.println((Integer) redisService.get("age"));
        redisService.set("age", 18);
        System.out.println((Integer) redisService.get("age"));
        redisService.remove("age");
        System.out.println((Integer) redisService.get("age"));

        System.out.println(redisService.contains("redis"));
        redisService.set("redis", "redis");
        System.out.println(redisService.contains("redis"));

        User user = new User();
        user.setName("Oven");
        user.setAge(18);
        user.setScore(88.88);

        System.out.println((User) redisService.get("user"));
        redisService.set("user", user);
        System.out.println((User) redisService.get("user"));
        redisService.remove("user");
        System.out.println((User) redisService.get("user"));

        return "测试完毕";
    }

    @RequestMapping("/get")
    public String get() {
        return author;
    }

}
