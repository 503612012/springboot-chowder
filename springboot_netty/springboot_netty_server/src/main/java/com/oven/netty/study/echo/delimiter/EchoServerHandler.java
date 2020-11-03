package com.oven.netty.study.echo.delimiter;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EchoServerHandler extends ChannelHandlerAdapter {

    /**
     * MessagePackServerHandler 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(EchoServerHandler.class);

    /**
     * 计数器
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info("--- [发生异常] 释放资源");
        ctx.close();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        String body = (String) msg;
        LOGGER.info("--- [第{}次接收客户端消息] {}", ++counter, body);
        body += "$_$";
        ByteBuf echo = Unpooled.copiedBuffer(body.getBytes());
        ctx.writeAndFlush(echo);
    }

}
