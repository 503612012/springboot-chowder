package com.oven.service;

import com.oven.entity.User;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@CacheConfig(cacheNames = "user")
public class UserService {

    @Cacheable
    public User getById(Integer id) {
        System.out.println("查询数据库了。。。");
        return new User(id, "admin", "123", 18);
    }

    @CacheEvict
    public void delete(Integer id) {
        System.out.println("删除[" + id + "]缓存");
    }

}