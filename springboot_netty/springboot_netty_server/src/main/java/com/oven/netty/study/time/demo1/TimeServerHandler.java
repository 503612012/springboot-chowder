package com.oven.netty.study.time.demo1;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Date;

/**
 * 针对网络事件进行读写操作的类
 */
public class TimeServerHandler extends ChannelHandlerAdapter {

    /**
     * TimeServerHandler 日志控制器
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(TimeServerHandler.class);

    /**
     * 模拟粘包/拆包问题计数器
     */
    private int counter;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        ctx.close();
    }

    /**
     * 读事件
     *
     * @param ctx ChannelHandlerContext
     * @param msg 消息
     */
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // 将消息转换成ByteBuf
        ByteBuf buf = (ByteBuf) msg;
        // 获取缓冲区中的字节数
        byte[] req = new byte[buf.readableBytes()];
        buf.readBytes(req);
        String body = new String(req, StandardCharsets.UTF_8);
        LOGGER.info("--- [接收到的数据] {}", body);
        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() :
                "BAD ORDER";
        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());

        // region 模拟粘包/拆包问题相关代码
//        String body = new String(req, "utf-8").substring(0, req.length - System.getProperty("line.separator").length());
//        LOGGER.info("--- [接收到的数据] {} | [counter] {}", body, ++counter);
//        String currentTime = "QUERY TIME ORDER".equalsIgnoreCase(body) ? new Date(System.currentTimeMillis()).toString() : "BAD ORDER";
//        currentTime = currentTime + System.getProperty("line.separator");
//        ByteBuf resp = Unpooled.copiedBuffer(currentTime.getBytes());
        // endregion


        // 异步发送应答消息给Client
        ctx.write(resp); // --> 将消息放到发送缓冲数组中
    }

    /**
     * 读完之后
     */
    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        LOGGER.info("--- [服务器写消息] ");
        // 将消息发送队列中的消息写到SocketChannel中
        ctx.flush(); // --> 将消息写到 SocketChannel 中
    }

}
