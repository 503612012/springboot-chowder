package com.oven.controller;

import com.oven.limitation.Limit;
import com.oven.limitation.LimitType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DemoController {

    @RequestMapping("/limit")
    @Limit(key = "limit.key", period = 5, count = 5, errMsg = "限流异常", limitType = LimitType.IP_AND_METHOD)
    public String limit() {
        return "hello...";
    }

}
