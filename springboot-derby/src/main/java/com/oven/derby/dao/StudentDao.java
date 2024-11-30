package com.oven.derby.dao;

import com.oven.derby.entity.Student;
import com.oven.derby.utils.PropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import java.util.List;

@Repository
public class StudentDao {

    @Resource
    private JdbcTemplate jdbcTemplate;

    public void create() {
        jdbcTemplate.execute("create table t_student" +
                "(" +
                "    dbid   int generated always as identity (start with 1, increment by 1) primary key," +
                "    name   varchar(32) not null," +
                "    age    int," +
                "    gender int," +
                "    phone  varchar(16) not null" +
                ")");
    }

    public void add() {
        String sql = "insert into t_student (name, age, gender, phone) values (?, ?, ?, ?)";
        jdbcTemplate.update(sql, "张三", 18, 1, "15700000001");
    }

    public void update() {
        String sql = "update t_student set name = ?, age = ?, gender = ?, phone = ? where dbid = ?";
        jdbcTemplate.update(sql, "张三2", 19, 0, "15700000002", 1);
    }

    public void delete(Integer id) {
        String sql = "delete from t_student where dbid = ?";
        jdbcTemplate.update(sql, id);
    }

    public List<Student> list(String like) {
        if (!StringUtils.isEmpty(like)) {
            // 'CONCAT' is not recognized as a function or procedure.
            return jdbcTemplate.query("select * from t_student where name like concat('%', ?, '%')", PropertyRowMapper.build(Student.class), like);
        }
        return jdbcTemplate.query("select * from t_student", PropertyRowMapper.build(Student.class));
    }

}
