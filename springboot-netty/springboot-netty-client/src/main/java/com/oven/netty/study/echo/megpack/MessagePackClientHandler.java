package com.oven.netty.study.echo.megpack;

import com.oven.netty.entity.User;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessagePackClientHandler extends ChannelHandlerAdapter {


    private static final Logger LOGGER = LoggerFactory.getLogger(MessagePackClientHandler.class);

    private final int sendNumber;

    MessagePackClientHandler(int sendNumber) {
        this.sendNumber = sendNumber;
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        LOGGER.error("--- [异常] {}", cause.getMessage());
        ctx.close();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        User[] userInfo = UserInfo();
        ctx.writeAndFlush(userInfo);
        for (User info : userInfo) {
            ctx.write(info);
        }
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        LOGGER.info("--- [收到服务器的消息] {}", msg);
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    private User[] UserInfo() {
        User[] users = new User[sendNumber];
        User user;
        for (int i = 0; i < sendNumber; i++) {
            user = new User();
            user.setId(i);
            user.setName("Oven --->" + i);
            users[i] = user;
        }
        return users;
    }

}
