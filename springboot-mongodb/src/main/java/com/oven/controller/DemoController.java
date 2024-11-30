package com.oven.controller;

import com.oven.entity.User;
import com.oven.service.UserService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoController {

    @Resource
    private UserService userService;

    @RequestMapping("/add")
    public String add(User user) {
        userService.save(user);
        return "添加成功";
    }

    @RequestMapping("/findAll")
    public List<User> findAll() {
        return userService.findAll();
    }

    @RequestMapping("/delete")
    public String delete(Integer id) {
        userService.delete(id);
        return "删除成功";
    }

}
