package com.oven.controller;

import com.oven.context.ServiceContext;
import com.oven.service.IService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class DemoController {

    @Resource
    private ServiceContext serviceContext;

    @RequestMapping("/test")
    public Object test(String serviceId) {
        IService service = serviceContext.getService(serviceId);
        return service.doSomething();
    }

}
