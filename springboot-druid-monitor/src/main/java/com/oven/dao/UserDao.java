package com.oven.dao;

import com.oven.entity.User;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Repository
public class UserDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void add(User user) {
        jdbcTemplate.update("insert into t_user (dbid, uname, pwd, age) value (null, ?, ?, ?)", user.getUname(), user.getPwd(), user.getAge());
    }

    public List<User> get() {
        return jdbcTemplate.query("select * from t_user", userRowMapper());
    }

    private RowMapper<User> userRowMapper() {
        return new RowMapper<User>() {
            @Override
            public User mapRow(ResultSet rs, int rowNum) throws SQLException {
                User user = new User();
                user.setId(rs.getInt("dbid"));
                user.setUname(rs.getString("uname"));
                user.setPwd(rs.getString("pwd"));
                user.setAge(rs.getInt("age"));
                return user;
            }
        };
    }

}
