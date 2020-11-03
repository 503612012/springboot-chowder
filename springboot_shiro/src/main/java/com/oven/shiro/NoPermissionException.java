package com.oven.shiro;

import org.apache.shiro.authz.AuthorizationException;
import org.apache.shiro.authz.UnauthorizedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class NoPermissionException {

    @ExceptionHandler(UnauthorizedException.class)
    public String handleShiroException(Exception e) {
        e.printStackTrace();
        return "/noauth";
    }

    @ExceptionHandler(AuthorizationException.class)
    public String AuthorizationException(Exception e) {
        e.printStackTrace();
        return "/noauth";
    }

}