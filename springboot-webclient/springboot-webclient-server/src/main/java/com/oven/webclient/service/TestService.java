package com.oven.webclient.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Slf4j
@Service
public class TestService {

    public JSONObject test() {
        JSONObject obj = new JSONObject();
        try {
            log.info("调用了webclient-server测试接口");
            for (int i = 1; i <= 10; i++) {
                log.info("等待【{}}】...", i);
                TimeUnit.SECONDS.sleep(1);
            }
            obj.put("code", 200);
            obj.put("msg", "success");
        } catch (Exception e) {
            log.error("webclient-server异常：", e);
            obj.put("code", 500);
            obj.put("msg", "fail");
        }
        return obj;
    }

}
