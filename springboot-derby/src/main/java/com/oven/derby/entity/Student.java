package com.oven.derby.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Id;

@Data
public class Student {

    @Id
    @Column(name = "dbid")
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender")
    private Integer gender;

    @Column(name = "phone")
    private String phone;

}
