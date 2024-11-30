package com.oven.service.impl;

import com.oven.service.DemoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@Service(version = "1.0.0")
public class DemoServiceImpl implements DemoService {

    @Override
    public String sayHello(String name) {
        log.info("dubbo服务端被调用了：{}", name);
        return "hello " + name;
    }

}
