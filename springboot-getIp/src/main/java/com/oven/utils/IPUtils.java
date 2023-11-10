package com.oven.utils;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

/**
 * IP工具类
 *
 * @author Oven
 */
public class IPUtils {

    public static String getClientIPAddr(HttpServletRequest request) {
        String ip;
        // 1.首先考虑有反向代理的情况，如果有代理，通过“x-forwarded-for”获取真实ip地址
        ip = request.getHeader("x-forwarded-for");
        // 2.如果squid.conf的配制文件forwarded_for项默认是off，则：X-Forwarded-For：unknown。考虑用Proxy-Client-IP或WL-Proxy-Client-IP获取
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        // 3.最后考虑没有代理的情况，直接用request.getRemoteAddr()获取ip
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
            // 使用localhost访问
            if ("0:0:0:0:0:0:0:1".equals(ip)) {
                ip = "127.0.0.1";
            }
        }
        // 4.如果通过多级反向代理，则可能产生多个ip，其中第一个非unknown的IP为客户端真实IP（IP按照','分割）
        if (ip != null && ip.split(",").length > 1) {
            ip = (ip.split(","))[0];
        }
        // 5.如果是服务器本地访问，需要根据网卡获取本机真实ip
        if ("127.0.0.1".equals(ip)) {
            try {
                ip = InetAddress.getLocalHost().getHostAddress();
            } catch (UnknownHostException e) {
                e.printStackTrace();
            }
        }
        // 6.校验ip的合法性，不合法返回""
        if (!isValidIp(ip)) {
            return "";
        } else {
            return ip;
        }
    }

    /**
     * 判断是否为合法IP地址
     *
     * @param ipAddress ip地址
     */
    private static boolean isValidIp(String ipAddress) {
        boolean retVal = false;
        try {
            if (ipAddress != null && !"".equals(ipAddress)) {
                String regex = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
                Pattern pattern = Pattern.compile(regex);
                retVal = pattern.matcher(ipAddress).matches();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return retVal;
    }

}
