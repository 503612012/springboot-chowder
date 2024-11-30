package com.oven.config.entity;

import lombok.Data;

@Data
public class DataSourceConfig {

    private Long id;
    private String name;
    private String url;
    private String username;
    private String password;
    private String driverClassName;

}
