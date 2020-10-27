package com.oven.service.impl;

import com.oven.service.IService;
import org.springframework.stereotype.Service;

@Service
public class Service02Impl implements IService {

    @Override
    public String serviceId() {
        return "02";
    }

    @Override
    public String doSomething() {
        return "hello 02";
    }

}