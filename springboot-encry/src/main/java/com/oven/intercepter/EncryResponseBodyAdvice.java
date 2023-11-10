package com.oven.intercepter;

import com.alibaba.fastjson.JSON;
import com.oven.util.DesUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * 请求参数-加密操作
 *
 * @author Oven
 */
@Component
@ControllerAdvice(basePackages = "com.oven.controller")
public class EncryResponseBodyAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter methodParameter, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object obj,
                                  MethodParameter methodParameter,
                                  MediaType mediaType,
                                  Class<? extends HttpMessageConverter<?>> converterType,
                                  ServerHttpRequest request,
                                  ServerHttpResponse response) {
        String result = "";
        try {
            // 添加encry header，告诉前端数据已加密
            response.getHeaders().add("encry", "true");
            String srcData = JSON.toJSONString(obj);
            // 加密
            result = DesUtil.encrypt(srcData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}