package com.oven.mapper.mysql;

import com.oven.entity.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author Oven
 */
public interface UserMySQLMapper {

    List<User> findAll();

}
