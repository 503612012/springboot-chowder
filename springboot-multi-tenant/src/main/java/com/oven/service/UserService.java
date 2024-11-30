package com.oven.service;

import com.oven.dao.UserDao;
import com.oven.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public List<User> get() {
        return userDao.get();
    }

}
