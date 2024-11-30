package com.oven.okhttp.service;

import com.oven.okhttp.entity.Student;
import org.springframework.stereotype.Service;

@Service
public class StudentService {

    public Student get(Integer id, Student student) {
        student.setId(id);
        return student;
    }

}
