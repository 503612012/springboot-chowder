package com.oven.netty.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * 记录调用方法的元信息
 */
@Data
@ToString
@NoArgsConstructor
@EqualsAndHashCode
public class MethodInvokeMeta implements Serializable {

    private static final long serialVersionUID = 5429914235135594820L;

    /**
     * 接口
     */
    private Class<?> interfaceClass;

    /**
     * 方法名
     */
    private String methodName;

    /**
     * 参数
     */
    private Object[] args;

    /**
     * 返回值类型
     */
    private Class<?> returnType;

    /**
     * 参数类型
     */
    private Class<?>[] parameterTypes;

}
