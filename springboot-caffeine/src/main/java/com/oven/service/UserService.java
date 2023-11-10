package com.oven.service;

import com.oven.vo.User;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

/**
 * 用户服务层
 *
 * @author Oven
 */
@Service
public class UserService {

    @CachePut(value = "user", key = "#user.id")
    public User save(User user) {
        System.out.println("操作数据库，保存用户，" + user.toString());
        System.out.println("添加key为[" + user.getId() + "]的缓存");
        return user;
    }

    @CacheEvict(value = "user", key = "#id")
    public void delete(Integer id) {
        System.out.println("添加key为[" + id + "]的缓存");
    }

    @Cacheable(value = "user", key = "#id", sync = true)
    public User getById(Integer id) {
        System.out.println("操作数据库，进行通过ID查询，ID: " + id);
        User user = new User();
        user.setId(id);
        user.setAge(18);
        user.setUserName("Oven");
        System.out.println("添加key为[" + id + "]的缓存");
        return user;
    }

}
