package com.oven.limitation;

public enum LimitType {

    /**
     * ip+方法名
     */
    IP_AND_METHOD,

    /**
     * 自定义key
     */
    CUSTOMER,

    /**
     * 根据请求者IP
     */
    IP

}
