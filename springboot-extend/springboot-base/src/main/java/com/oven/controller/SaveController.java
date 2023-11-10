package com.oven.controller;

import com.oven.service.ISaveService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class SaveController {

    @Resource
    private ISaveService saveService;

    @RequestMapping("/save")
    public String save() {
        saveService.save();
        return "true";
    }

}
