package com.oven.okhttp.controller;

import com.alibaba.fastjson.JSONObject;
import com.oven.okhttp.entity.Student;
import com.oven.okhttp.service.StudentService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/student")
public class StudentController {

    @Resource
    private StudentService studentService;

    @RequestMapping("/get")
    public Object get(Integer id, @RequestBody Student student) {
        JSONObject result = new JSONObject();
        result.put("code", 200);
        result.put("data", studentService.get(id, student));
        return result;
    }

}
