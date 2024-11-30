package com.oven.user.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.oven.user.entity.User;
import com.oven.user.mapper.UserMapper;
import com.oven.user.service.IUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void test() {
        User user = User.builder().uname("admin").pwd("123456").age(18).build();
        userMapper.insert(user);
        int id = user.getId();
        log.info("添加用户的id为：{}", id);

        user = userMapper.selectById(id);
        log.info("根据id [{}] 获取到的用户信息为：{}", id, user);

        user.setAge(19);
        userMapper.updateById(user);

        user = userMapper.selectById(id);
        log.info("修改后获取到的用户信息为：{}", user);

        QueryWrapper<User> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uname", "admin");
        user = userMapper.selectOne(queryWrapper);
        log.info("根据用户名获取到的用户信息为：{}", user);

        int res = userMapper.deleteById(id);
        log.info("删除用户返回结果：{}", res);

        user = userMapper.selectById(id);
        log.info("删除后根据id [{}] 获取到的用户信息为：{}", id, user);

        for (int i = 0; i < 27; i++) {
            userMapper.insert(User.builder().uname("admin").pwd("123456").age(i).build());
        }
        // 分页获取用户
        PageHelper.startPage(2, 10);
        PageHelper.orderBy("dbid");
        List<User> userList = userMapper.selectList(null);
        log.info("获取到的用户列别为：{}", userList);
        PageHelper.clearPage();
    }

}
