package com.oven.controller;

import com.oven.dao.UserRepository;
import com.oven.vo.User;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryStringQueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

    @Autowired
    UserRepository userRepository;

    @GetMapping("/addUser")
    public String addUser(String username, String password, Integer age, Integer id) {
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setPassword(password);
        user.setAge(age);
        return String.valueOf(userRepository.index(user).getId());// 返回id做验证
    }

    @GetMapping("/deleteUser")
    public String deleteUser(Integer id) {
        // userRepository.delete(id);
        return "Success!";
    }

    @GetMapping("/updateUser")
    public String updateUser(Integer id, String username, String password, Integer age) {
        User user = new User();
        user.setId(id);
        user.setUserName(username);
        user.setPassword(password);
        user.setAge(age);
        return String.valueOf(userRepository.save(user).getId());// 返回id做验证
    }

    @GetMapping("/getUser")
    public User getUser(Integer id) {
        // return userRepository.findOne(id);
        return userRepository.findById(id).get();
    }

    @GetMapping("/getAllUsers")
    public Iterable<User> getAllUsers() {
        return userRepository.findAll();
    }

    private static int PAGE_SIZE = 1;
    private static int PAGE_NUMBER = 10;

    /**
     * 在ES中搜索内容
     */
    @GetMapping("/search")
    public Object searchEntity(String key) {
        QueryBuilder query = new QueryStringQueryBuilder(key);
        return userRepository.search(query);
    }

}
