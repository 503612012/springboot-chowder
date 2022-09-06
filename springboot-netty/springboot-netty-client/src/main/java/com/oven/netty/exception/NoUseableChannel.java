package com.oven.netty.exception;

/**
 * 没有可用的通道异常
 */
public class NoUseableChannel extends RuntimeException{

    private static final long serialVersionUID = 7762465537123947683L;

    public NoUseableChannel() {
        super();
    }

    public NoUseableChannel(String message) {
        super(message);
    }

}
