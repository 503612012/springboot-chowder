package com.oven.controller;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private RestTemplate restTemplate;

    @RequestMapping("/get")
    public String get() {
        String url = "http://localhost:8080/get?id={id}&uname={uname}&pwd={pwd}&age={age}";
        return restTemplate.getForObject(url, String.class, 1, "Oven", "123456", 18);
    }

    @RequestMapping("/post")
    public String post() {
        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("id", "1");
        params.add("uname", "Oven");
        params.add("pwd", "123456");
        params.add("age", "18");
        return restTemplate.postForObject("http://localhost:8080/post", params, String.class);
    }

}
