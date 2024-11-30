package com.oven.service;

import com.oven.entity.User;
import com.oven.mapper.mysql.MySqlMapper;
import com.oven.mapper.oracle.OracleMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private MySqlMapper mySqlMapper;
    @Resource
    private OracleMapper oracleMapper;

    public List<User> getMysql() {
        return mySqlMapper.getUser();
    }

    public List<User> getOracle() {
        return oracleMapper.getUser();
    }

}
