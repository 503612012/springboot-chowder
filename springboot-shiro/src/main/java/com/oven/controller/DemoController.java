package com.oven.controller;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class DemoController {

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/noauth")
    public String noauth() {
        return "noauth";
    }

    @RequestMapping("/doLogin")
    public String doLogin(String userName, String password) {
        try {
            System.out.println(userName + "-" + password);
            Subject subject = SecurityUtils.getSubject();
            UsernamePasswordToken token = new UsernamePasswordToken(userName, password);
            subject.login(token);
            return "redirect:/";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
