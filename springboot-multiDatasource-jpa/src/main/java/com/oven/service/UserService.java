package com.oven.service;

import com.oven.dao.mysql.MysqlUserDao;
import com.oven.dao.oracle.OracleUserDao;
import com.oven.entity.User;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class UserService {

    @Resource
    private MysqlUserDao mysqlUserDao;
    @Resource
    private OracleUserDao oracleUserDao;

    public List<User> getMysql() {
        return mysqlUserDao.findAll();
    }

    public List<User> getOracle() {
        return oracleUserDao.findAll();
    }

}
