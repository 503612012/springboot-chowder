package com.oven.controller;

import com.oven.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getById")
    public Object getById(Integer id) {
        return userService.getById(id);
    }

    @RequestMapping("/delete")
    public String delete(Integer id) {
        userService.delete(id);
        return "删除成功";
    }

}
