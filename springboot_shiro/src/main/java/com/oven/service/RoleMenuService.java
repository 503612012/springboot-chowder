package com.oven.service;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RoleMenuService {

    public List<String> findByUserId(Integer id) {
        System.out.println("此处省略查询数据库过程" + id);
        List<String> result = new ArrayList<>();
        result.add("user:list");
        result.add("user:add");
        result.add("user:update");
        return result;
    }

}
