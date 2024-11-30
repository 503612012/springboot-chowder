package com.oven.mapper.mysql;

import com.oven.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface MySqlMapper {

    List<User> getUser();

}
