package com.oven.mapper.oracle;

import com.oven.vo.User;

import java.util.List;

/**
 * UserMapper
 *
 * @author Oven
 */
public interface UserOracleMapper {

    List<User> findAll();

}
