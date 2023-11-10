package com.oven.mapper;

import com.oven.entity.RolePermisson;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface PermissionMapper {

    @Select("select r.name as rolename, p.url from role r left join role_permission rp on r.id = rp.role_id left join permission p on rp.permission_id = p.id")
    List<RolePermisson> getRolePermissions();

}
