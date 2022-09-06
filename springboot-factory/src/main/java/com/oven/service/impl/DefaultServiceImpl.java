package com.oven.service.impl;

import com.oven.service.IService;
import org.springframework.stereotype.Service;

@Service
public class DefaultServiceImpl implements IService {

    @Override
    public String serviceId() {
        return "00";
    }

    @Override
    public String doSomething() {
        return "hello 00";
    }

}