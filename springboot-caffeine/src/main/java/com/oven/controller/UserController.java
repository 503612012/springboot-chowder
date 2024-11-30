package com.oven.controller;

import com.oven.entity.User;
import com.oven.service.UserService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

/**
 * 用户控制器
 *
 * @author Oven
 */
@Controller
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 通过ID查询
     */
    @RequestMapping("/getById")
    @ResponseBody
    public Object getById(Integer id) {
        return userService.getById(id);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    @ResponseBody
    public Object save(User user) {
        return userService.save(user);
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    @ResponseBody
    public Object delete(Integer id) {
        userService.delete(id);
        return "删除成功！";
    }

}
