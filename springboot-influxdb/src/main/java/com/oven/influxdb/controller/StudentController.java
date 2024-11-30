package com.oven.influxdb.controller;

import com.oven.influxdb.config.InfluxDbTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;

@RestController
public class StudentController {

    @Resource
    private InfluxDbTemplate influxDbTemplate;

    @RequestMapping("/insert")
    public Object insert(Integer id, String name, Integer age, String phone) {
        java.util.Map<String, String> tags = new HashMap<>();
        tags.put("index", "name");
        tags.put("id", "id");
        Map<String, Object> fields = new HashMap<>();
        fields.put("id", id);
        fields.put("name", name);
        fields.put("age", age);
        fields.put("phone", phone);
        influxDbTemplate.write("test", "t_student", tags, fields);
        return "添加成功";
    }

    @RequestMapping("/list")
    public Object list() {
        // return influxDbTemplate.query("select * from t_student", "test", Student.class);
        return influxDbTemplate.query("select * from t_student", "test");
    }

}
