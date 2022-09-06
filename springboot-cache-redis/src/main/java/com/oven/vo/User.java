package com.oven.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class User implements Serializable {

    private Integer id;
    private String uname;
    private String pwd;
    private Integer age;

}
