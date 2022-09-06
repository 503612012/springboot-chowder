package com.oven.limitation;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 自定义限流异常类
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class LimitException extends RuntimeException {

    private static final long serialVersionUID = -1920795727304163167L;

    private Integer code;
    private String msg;

    LimitException(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

}
