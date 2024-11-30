package com.oven.webclient.controller;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

import javax.annotation.Resource;

@Slf4j
@RestController
public class TestController {

    @Resource
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/test")
    public Object test() {
        log.info("开始测试");
        WebClient webClient = webClientBuilder.baseUrl("http://127.0.0.1:8080").build();

        Flux<String> flux = webClient.post()
                .uri("/test")
                .contentType(MediaType.APPLICATION_JSON)
                // .body(null)
                .retrieve()
                .bodyToFlux(String.class);

        flux.subscribe(value -> log.info("调用接口返回内容：{}", value), error -> log.info("调用接口异常：", error));
        log.info("测试结束");
        return JSONObject.parse("{'code': 200, 'msg': 'success'}");
    }

}
