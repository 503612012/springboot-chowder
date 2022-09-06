package com.oven.util;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

public class ParametersUtils {

    public static String getParameters(HttpServletRequest req) {
        Enumeration<String> enums = req.getParameterNames();
        JSONObject parameter = new JSONObject();
        while (enums.hasMoreElements()) {
            String name = enums.nextElement();
            parameter.put(name, req.getParameter(name));
        }
        return parameter.toJSONString();
    }

}
