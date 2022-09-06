package com.oven.service.impl;

import com.oven.service.UserService;
import com.oven.vo.Gender;
import com.oven.vo.User;

import javax.jws.WebService;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@WebService(
        targetNamespace = "wsdl.oven.com", // wsdl命名空间
        name = "userPortType",                                   // portType名称，客户端生成代码时，为接口名称
        serviceName = "userService",                             // 服务name名称
        portName = "userPortName",                               // port名称
        endpointInterface = "com.oven.service.UserService"       // 指定发布webservcie的接口类，此类也需要接入@WebService注解
)
public class UserServiceImpl implements UserService {

    @Override
    public User getUserByName(String name) {
        User user = new User();
        user.setName(name);
        user.setAge(18);
        user.setGender(Gender.MALE);
        user.setHobby(Arrays.asList("吃饭", "睡觉"));
        return user;
    }

    @Override
    public List<User> getList() {
        List<User> result = new ArrayList<>();
        User user = new User();
        user.setName("Oven");
        user.setAge(18);
        user.setGender(Gender.MALE);
        user.setHobby(Arrays.asList("吃饭", "睡觉"));
        result.add(user);
        result.add(user);
        return result;
    }

    @Override
    public String getString(String msg) {
        return "hello " + msg;
    }

}
