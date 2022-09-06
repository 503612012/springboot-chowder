package com.oven.service;

import com.oven.mapper.UserMapper;
import com.oven.vo.User;
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

}
