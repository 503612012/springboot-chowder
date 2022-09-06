package com.oven.exception;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

@ResponseBody
@ControllerAdvice
public class GlobalExceptionHandle {

    /**
     * 处理捕获的异常
     */
    @ExceptionHandler(value = Exception.class)
    public Object handleException(HttpServletRequest request) {
        System.out.println("请求地址：" + request.getRequestURL().toString());
        System.out.println("请求方法：" + request.getMethod());
        System.out.println("请求者IP：" + request.getRemoteAddr());
        return "异常了";
    }

}
