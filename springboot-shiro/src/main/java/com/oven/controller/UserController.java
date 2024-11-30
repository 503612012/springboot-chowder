package com.oven.controller;

import com.oven.entity.User;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    @RequestMapping("/list")
    @RequiresPermissions("user:list")
    public List<User> list() {
        List<User> list = new ArrayList<>();
        list.add(new User(1, "Oven", "123456", 18));
        list.add(new User(2, "Oven", "123456", 18));
        return list;
    }

    @RequestMapping("/add")
    @RequiresPermissions("user:add")
    public String add() {
        return "添加成功";
    }

    @RequestMapping("/update")
    @RequiresPermissions("user:update")
    public String update() {
        return "修改成功";
    }

    @RequestMapping("/delete")
    @RequiresPermissions("user:delete")
    public String delete() {
        return "删除成功";
    }

}
