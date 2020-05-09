package com.oven.service;

import com.oven.dao.UserDao;
import com.oven.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private UserDao userDao;

    public void add(User user) {
        userDao.add(user);
    }

    public void delete(Integer id) {
        userDao.delete(id);
    }

    public void update(User user) {
        userDao.update(user);
    }

    public User getById(Integer id) {
        return userDao.getById(id);
    }

    public List<User> get() {
        return userDao.get();
    }

}