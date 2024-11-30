package com.oven.entity;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
public class User {

    @Id
    @Column(name = "dbid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "uname")
    private String uname;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "age")
    private Integer age;

}
