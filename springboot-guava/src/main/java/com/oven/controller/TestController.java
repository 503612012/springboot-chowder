package com.oven.controller;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/test")
    @Cacheable(value = "test")
    public Object test() {
        System.out.println("进入方法内部");
        return "hello guava";
    }

    @RequestMapping("/save")
    @CachePut(value = "name", key = "#name")
    public Object save(String name) {
        System.out.println("添加key为[" + name + "]的缓存");
        return name;
    }

    @RequestMapping("/delete")
    @CacheEvict(value = "name", key = "#name")
    public void delete(String name) {
        System.out.println("删除key为[" + name + "]的缓存");
    }

    @RequestMapping("/getByName")
    @Cacheable(value = "name", key = "#name")
    public Object getByName(String name) {
        System.out.println("添加key为[" + name + "]的缓存");
        return name;
    }

}
