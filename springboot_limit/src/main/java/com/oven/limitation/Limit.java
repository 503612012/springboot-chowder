package com.oven.limitation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 应用服务端限流注解
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Limit {

    /**
     * 资源的名字
     */
    String name() default "";

    /**
     * 资源的key
     */
    String key() default "";

    /**
     * Key的prefix
     */
    String prefix() default "";

    /**
     * 给定的时间段,单位秒
     */
    int period();

    /**
     * 最多的访问限制次数
     */
    int count();

    /**
     * 限流后返回的描述信息
     */
    String errMsg();

    /**
     * 类型
     */
    LimitType limitType() default LimitType.CUSTOMER;

}