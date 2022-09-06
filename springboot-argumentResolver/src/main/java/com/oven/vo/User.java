package com.oven.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class User {

    private Integer id;
    private String uname;
    private String pwd;
    private Integer age;

}
