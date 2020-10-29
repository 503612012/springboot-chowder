package com.oven.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/index")
    @SuppressWarnings({"NumericOverflow", "unused", "divzero"})
    public String index() {
        int i = 1 / 0;
        return "请求成功";
    }

    @RequestMapping("/err")
    public String err() {
        return "error";
    }

}
