package com.oven.controller;

import com.oven.wsdl.User;
import com.oven.wsdl.UserPortType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class DemoController {

    @Resource(name = "cxfProxy")
    private UserPortType userPortType;

    @GetMapping("/getString")
    public String getString(String msg) {
        return userPortType.getString(msg);
    }

    @GetMapping("/getUserByName")
    public User getUserByName(String name) {
        return userPortType.getUserByName(name);
    }

    @GetMapping("/getList")
    public List<User> getList() {
        return userPortType.getList();
    }

}
