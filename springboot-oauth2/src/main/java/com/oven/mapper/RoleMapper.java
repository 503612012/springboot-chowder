package com.oven.mapper;

import com.oven.entity.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface RoleMapper {

    @Select("select r.id, r.name from role r left join user_role ur on r.id = ur.role_id where ur.user_id = ${userId}")
    List<Role> getRolesByUserId(@Param("userId") Long userId);

}
