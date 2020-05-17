package com.oven.dao;

import com.oven.vo.User;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDao extends BaseDao {

    public List<User> get() {
        String sql = "select * from t_user";
        return super.getJdbcTemplate().query(sql, (rs, rowNum) -> {
            User user = new User();
            user.setId(rs.getInt("dbid"));
            user.setUname(rs.getString("uname"));
            user.setPwd(rs.getString("pwd"));
            user.setAge(rs.getInt("age"));
            return user;
        });
    }

}
