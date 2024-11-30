package com.oven.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "t_user")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
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
