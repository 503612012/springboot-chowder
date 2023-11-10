package com.oven.minio.controller;

import com.oven.minio.service.MinioService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final MinioService minioService;

    @GetMapping("/test2")
    public void test2() {
        minioService.makeBucket("person");
        minioService.makeBucket("person2");
        minioService.removeBucket("person2");
        minioService.remove("first-bucket", "Win11_22H2_Chinese_Simplified_x64v2.iso");
    }

}


