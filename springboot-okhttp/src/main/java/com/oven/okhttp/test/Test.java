package com.oven.okhttp.test;

import cn.hutool.http.HttpStatus;
import com.alibaba.fastjson.JSONObject;
import com.oven.okhttp.http.StudentServerApiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import retrofit2.Response;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Slf4j
@Component
public class Test implements CommandLineRunner {

    @Value("${remote.baseUrl.api.getStudent}")
    private String getStudentUrl;

    @Resource
    private StudentServerApiService studentServerApiService;

    @Override
    public void run(String... args) {
        try {
            TimeUnit.SECONDS.sleep(2);
            log.info("开始测试");

            Map<String, String> headers = new HashMap<>();
            headers.put("aaa", "bbb");

            Map<String, String> params = new HashMap<>();
            params.put("id", "27");

            JSONObject body = new JSONObject();
            body.put("name", "Oven");
            body.put("age", 18);
            body.put("phone", "15700000001");

            Response<String> response = studentServerApiService.getStudent(getStudentUrl, headers, params, body);
            if (Objects.isNull(response) || !response.isSuccessful()) {
                log.error("调用远程API异常");
            }
            log.info("调用远程API结果：body={}, response={}", response.body(), response);
            JSONObject responseObj = JSONObject.parseObject(response.body());
            if (HttpStatus.HTTP_OK != responseObj.getInteger("code")) {
                log.error("远程API接口返回异常：{}", responseObj.toJSONString());
            }
            log.info("测试完成");
        } catch (Exception e) {
            log.error("测试异常：", e);
        }
    }

}
