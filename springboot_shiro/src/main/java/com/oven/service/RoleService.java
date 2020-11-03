package com.oven.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleService {

    public List<String> findByUserId(Integer id) {
        System.out.println("此处省略查询数据库过程" + id);
        List<String> result = new ArrayList<>();
        result.add("超级管理员");
        result.add("普通用户");
        return result;
    }

}
