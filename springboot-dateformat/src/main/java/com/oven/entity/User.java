package com.oven.entity;

import lombok.Data;

import java.util.Date;

@Data
public class User {

    private Integer id;
    private String name;
    private Date createTime;

}
