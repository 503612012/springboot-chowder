package com.oven.mapper.oracle;

import com.oven.entity.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface OracleMapper {

    List<User> getUser();

}
