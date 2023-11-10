package com.oven.controller;

import com.oven.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 测试控制器
 *
 * @author Oven
 */
@RestController
public class TestController {

    @Resource
    private UserService userService;

    @RequestMapping("/findAll")
    public Object findAll(String dataSource) {
        return userService.findAll(dataSource);
    }

}
