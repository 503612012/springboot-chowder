package com.oven;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
public class DemoController {

    @RequestMapping("/test")
    public String index(String name) {
        return "hello " + name;
    }

}