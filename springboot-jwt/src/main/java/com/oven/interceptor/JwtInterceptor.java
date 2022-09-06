package com.oven.interceptor;

import com.alibaba.fastjson.JSON;
import com.oven.constants.AppConst;
import com.oven.utils.JwtUtils;
import com.oven.utils.ResultInfo;
import io.jsonwebtoken.Claims;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * jwtToken校验拦截器
 */
public class JwtInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String token = request.getHeader(JwtUtils.getHeaderKey()); // 获取用户token
        if (StringUtils.isBlank(token)) {
            token = request.getParameter(JwtUtils.getHeaderKey());
        }
        if (StringUtils.isBlank(token)) { // token为空
            this.writerErrorMsg(302, JwtUtils.getHeaderKey() + " can not be blank", response);
            return false;
        }
        // 校验并解析token，如果token过期或者篡改，则会返回null
        Claims claims = JwtUtils.verifyAndGetClaimsByToken(token);
        if (null == claims) {
            this.writerErrorMsg(303, JwtUtils.getHeaderKey() + "失效，请重新登录", response);
            return false;
        }
        // 校验通过后，设置用户信息到request里，在controller中从request域中获取用户信息
        request.setAttribute(AppConst.USER_INFO_KEY, claims);
        return true;
    }

    /**
     * 利用response直接输出错误信息
     */
    private void writerErrorMsg(Integer code, String msg, HttpServletResponse response) throws IOException {
        ResultInfo<String> result = new ResultInfo<>();
        result.setCode(code);
        result.setData(msg);
        response.setContentType("application/json;charset=UTF-8");
        response.getWriter().write(JSON.toJSONString(result));
    }

}