package com.oven.service;

import com.oven.dao.UserRepository;
import com.oven.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Service
public class UserService {

    @Resource
    private UserRepository userRepository;

    public void add(User user) {
        userRepository.save(user);
    }

    public void delete(Integer id) {
        userRepository.deleteById(id);
    }

    public void update(User user) {
        User userInDb = userRepository.getOne(user.getId());
        userInDb.setUname(user.getUname());
        userInDb.setPwd(user.getPwd());
        userInDb.setAge(user.getAge());
        userRepository.deleteById(user.getId());
        userRepository.save(userInDb);
    }

    public User getById(Integer id) {
        return userRepository.getOne(id);
    }

    public User getByUname(String uname) {
        return userRepository.getByUname(uname);
    }

}
