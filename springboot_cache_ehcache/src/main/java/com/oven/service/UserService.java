package com.oven.service;

import com.oven.vo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @CacheEvict(value = "user", key = "#id")
    public void delete(Integer id) {
        System.out.println("删除key为[" + id + "]的缓存");
    }

    @Cacheable(value = "user", key = "#id", sync = true)
    public User getById(Integer id) {
        System.out.println("操作数据库，进行通过ID查询，ID: " + id);
        return new User(id, "admin", "123", 18);
    }

}