package com.oven.mapper.oracle;

import com.oven.entity.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author Oven
 */
public interface UserOracleMapper {

    List<User> findAll();

}
