package com.oven.controller;

import com.oven.service.UserService;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class UserController {

    @Resource
    private UserService userService;

    @RequestMapping("/getById")
    public User getById(Integer id) {
        return userService.getById(id);
    }

    @RequestMapping("/delete")
    public String delete(Integer id) {
        userService.delete(id);
        return "删除成功";
    }

}