package com.oven.dao;

import com.oven.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class UserDao {

    @Resource(name = "mysqlJdbcTemplate")
    private JdbcTemplate mysqlJdbcTemplate;
    @Resource(name = "oracleJdbcTemplate")
    private JdbcTemplate oracleJdbcTemplate;

    public List<User> getMysql() {
        return mysqlJdbcTemplate.query("select * from t_user", userRowMapper());
    }

    public List<User> getOracle() {
        return oracleJdbcTemplate.query("select * from t_user", userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("dbid"));
            user.setUname(rs.getString("uname"));
            user.setPwd(rs.getString("pwd"));
            user.setAge(rs.getInt("age"));
            return user;
        };
    }

}
