package com.oven.netty.study.time.demo2;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * I/O事件处理类
 */
public class ChildChannelHandler extends ChannelInitializer<SocketChannel> {

    /**
     * ChildChannelHandler 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ChildChannelHandler.class);

    /**
     * 创建NioSocketChannel成功之后，进行初始化时，
     * 将ChannelHandler设置到ChannelPipeline中，
     * 同样，用于处理网络I/O事件
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        LOGGER.info("--- [通道初始化]");
        ch.pipeline().addLast(new TimeServerHandler());
    }

}