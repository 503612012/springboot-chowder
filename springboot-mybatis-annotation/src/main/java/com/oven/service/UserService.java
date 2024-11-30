package com.oven.service;

import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.oven.entity.User;
import com.oven.mapper.UserMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserMapper userMapper;

    public void add(User user) {
        userMapper.add(user);
    }

    public void delete(Integer id) {
        userMapper.delete(id);
    }

    public void update(User user) {
        userMapper.update(user);
    }

    public User getById(Integer id) {
        return userMapper.getById(id);
    }

    public User getByUname(String uname) {
        return userMapper.getByUname(uname);
    }

    public Page<User> getByPage(Integer pageNum, Integer pageSize) {
        PageHelper.startPage(pageNum, pageSize);
        return userMapper.getByPage();
    }

}