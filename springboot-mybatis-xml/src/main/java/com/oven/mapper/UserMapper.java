package com.oven.mapper;

import com.oven.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {

    void add(User user);

    void delete(Integer id);

    void update(User user);

    User getById(Integer id);

    User getByUname(String uname);

}
