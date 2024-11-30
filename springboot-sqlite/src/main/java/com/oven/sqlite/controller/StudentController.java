package com.oven.sqlite.controller;

import com.oven.sqlite.service.StudentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class StudentController {

    @Resource
    private StudentService studentService;

    @RequestMapping("/create")
    public Object create() {
        return studentService.create();
    }

    @RequestMapping("/add")
    public Object add() {
        return studentService.add();
    }

    @RequestMapping("/update")
    public Object update() {
        return studentService.update();
    }

    @RequestMapping("/delete")
    public Object delete(Integer id) {
        return studentService.delete(id);
    }

    @RequestMapping("/list")
    public Object list(String like) {
        return studentService.list(like);
    }

}
