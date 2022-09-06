package com.oven.service;

import com.oven.vo.User;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    public User findByUserName(String userName) {
        return new User(1, userName, "123456", 18);
    }

}
