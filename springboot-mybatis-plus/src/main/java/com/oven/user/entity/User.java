package com.oven.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Builder;
import lombok.Data;
import lombok.ToString;

@Data
@Builder
@ToString
@TableName("t_user")
public class User {

    @TableId(value = "dbid", type = IdType.AUTO)
    private Integer id;
    private String uname;
    private String pwd;
    private Integer age;

}
