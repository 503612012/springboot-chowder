package com.oven.controller;

import com.oven.vo.ResultInfo;
import com.oven.vo.User;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
public class DemoController {

    @GetMapping(value = "/test")
    public Map<String, String> test() {
        return Collections.singletonMap("message", "springboot_restdoc");
    }

    @GetMapping(value = "/getById")
    public ResultInfo<User> getById(Integer id) {
        User user = new User(id, "admin", "123456", 18);
        return new ResultInfo<>(200, "请求成功", user);
    }

    @PostMapping(value = "/getAll")
    public ResultInfo<List<User>> getAll(User user) {
        List<User> list = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
            list.add(new User(i + 1, user.getUname() + i, "123456", user.getAge()));
        }
        return new ResultInfo<>(200, "请求成功", list);
    }

    @GetMapping(value = "/delete/{id}")
    public ResultInfo<String> delete(@PathVariable("id") int id) {
        return new ResultInfo<>(200, "删除成功", "删除用户【" + id + "】成功");
    }

}
