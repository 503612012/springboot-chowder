package com.oven.derby.service;

import com.oven.derby.dao.StudentDao;
import com.oven.derby.entity.Student;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class StudentService {

    @Resource
    private StudentDao studentDao;

    public String create() {
        studentDao.create();
        return "创建成功";
    }

    public String add() {
        studentDao.add();
        return "添加成功";
    }

    public String update() {
        studentDao.update();
        return "更新成功";
    }

    public String delete(Integer id) {
        studentDao.delete(id);
        return "删除成功";
    }

    public List<Student> list(String like) {
        return studentDao.list(like);
    }

}
