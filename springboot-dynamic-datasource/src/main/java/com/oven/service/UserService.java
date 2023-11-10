package com.oven.service;

import com.oven.mapper.mysql.UserMySQLMapper;
import com.oven.mapper.oracle.UserOracleMapper;
import com.oven.vo.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * 用户服务层
 *
 * @author Oven
 */
@Service
public class UserService {

    @Resource
    private UserMySQLMapper userMySQLMapper;
    @Resource
    private UserOracleMapper userOracleMapper;

    public List<User> findAll(String dataSource) {
        if ("mysql".equals(dataSource)) {
            return userMySQLMapper.findAll();
        } else {
            return userOracleMapper.findAll();
        }
    }

}
