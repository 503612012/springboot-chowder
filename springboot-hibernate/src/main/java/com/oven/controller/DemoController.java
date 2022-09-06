package com.oven.controller;

import com.oven.service.UserService;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private UserService userService;

    @RequestMapping("/add")
    public Object add(User user) {
        userService.add(user);
        return "添加成功";
    }

    @RequestMapping("/delete")
    public Object delete(Integer id) {
        userService.delete(id);
        return "删除成功";
    }

    @RequestMapping("/update")
    public Object update(User user) {
        userService.update(user);
        return "修改成功";
    }

    @RequestMapping("/getById")
    public Object getById(Integer id) {
        return userService.getById(id);
    }

    @RequestMapping("/getByUname")
    public Object getByUname(String uname) {
        return userService.getByUname(uname);
    }

}
