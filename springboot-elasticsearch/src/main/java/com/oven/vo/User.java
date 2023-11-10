package com.oven.vo;

import lombok.Data;
import org.springframework.data.elasticsearch.annotations.Document;

@Data
@Document(indexName = "users", type = "user")
public class User {

    private Integer id;
    private String userName;
    private String password;
    private Integer age;

}
