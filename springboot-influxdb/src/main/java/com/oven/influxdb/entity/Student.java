package com.oven.influxdb.entity;

import lombok.Data;

import java.util.Map;

@Data
public class Student {

    private Integer id;
    private String name;
    private Integer age;
    private String phone;

    private String time;
    private Map<String, Object> fields;

}
