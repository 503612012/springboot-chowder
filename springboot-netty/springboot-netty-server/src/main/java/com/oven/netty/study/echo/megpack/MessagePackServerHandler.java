package com.oven.netty.study.echo.megpack;

import com.oven.netty.entity.User;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class MessagePackServerHandler extends ChannelHandlerAdapter {

    /**
     * MessagePackServerHandler 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePackServerHandler.class);

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.info("--- [发生异常] 释放资源: {}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        ctx.writeAndFlush("Server connect success");
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        List<User> userInfo = (List<User>) msg;
        LOGGER.info("\n\t⌜⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓\n" +
                "\t├ [接收 ]: {}\n" +
                "\t⌞⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓⎓", userInfo);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

}
