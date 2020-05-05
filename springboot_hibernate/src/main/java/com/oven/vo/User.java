package com.oven.vo;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity
@Table(name = "t_user")
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer"})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;
    @Column(name = "uname")
    private String uname;
    @Column(name = "pwd")
    private String pwd;
    @Column(name = "age")
    private Integer age;

}
