package com.oven.mapper;

import com.github.pagehelper.Page;
import com.oven.vo.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("insert into t_user (dbid, uname, pwd, age) value (null, #{uname}, #{pwd}, #{age})")
    void add(User user);

    @Delete("delete from t_user where dbid = #{id}")
    void delete(Integer id);

    @Update("update t_user set uname=#{uname}, pwd=#{pwd}, age=#{age} where dbid=#{id}")
    void update(User user);

    @Select("select * from t_user where dbid = #{id}")
    @Results(value = {
            @Result(column = "dbid", property = "id")
    })
    User getById(Integer id);

    @Select("select * from t_user where uname = #{uname}")
    @Results(value = {
            @Result(column = "dbid", property = "id")
    })
    User getByUname(String uname);

    @Select("select * from t_user")
    @Results(value = {
            @Result(column = "dbid", property = "id")
    })
    Page<User> getByPage();

}