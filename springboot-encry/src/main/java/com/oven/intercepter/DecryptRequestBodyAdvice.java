package com.oven.intercepter;

import com.oven.util.DesUtil;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * 请求参数-解密操作
 *
 * @author Oven
 */
@Component
@ControllerAdvice(basePackages = "com.oven.controller")
public class DecryptRequestBodyAdvice implements RequestBodyAdvice {

    @Override
    public boolean supports(MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter methodParameter, Type targetType, Class<? extends HttpMessageConverter<?>> selectedConverterType) {
        return inputMessage;
    }

    @Override
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage, MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        String dealData = null;
        try {
            // 解密操作
            Map<String, String> dataMap = (Map) body;
            String srcData = dataMap.get("data");
            dealData = DesUtil.decrypt(srcData);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dealData;
    }

    @Override
    public Object handleEmptyBody(@Nullable Object object, HttpInputMessage inputMessage, MethodParameter methodParameter, Type type, Class<? extends HttpMessageConverter<?>> converterType) {
        return object;
    }

}
