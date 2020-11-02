package com.oven.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

@RestController
public class DemoController {

    @RequestMapping("/set")
    public String set(HttpSession session) {
        session.setAttribute("name", "Oven");
        return "设置成功";
    }

    @RequestMapping("/get")
    public String get(HttpSession session) {
        return (String) session.getAttribute("name");
    }

}
