package com.oven.netty.study.time.demo3;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.LineBasedFrameDecoder;
import io.netty.handler.codec.string.StringDecoder;
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

    /*
     * LineBasedFrameDecoder: 以换行符为结束标志的解码器
     *  依次遍历ByteBuf中的可读字节，
     *  如果有"\n" 或者 "\r\n" -> 就以此位置为结束位置 之后将可读索引到结束位置区间的字节组成一行
     */

    /**
     * 创建NioSocketChannel成功之后，进行初始化时，
     * 将ChannelHandler设置到ChannelPipeline中，
     * 同样，用于处理网络I/O事件
     */
    @Override
    protected void initChannel(SocketChannel ch) {
        LOGGER.info("--- [通道初始化]");
        // region 解决粘包/拆包问题相关代码
        ch.pipeline().addLast(new LineBasedFrameDecoder(1024));
        // 将接收到的对象转成字符串
        ch.pipeline().addLast(new StringDecoder());
        // endregion

        ch.pipeline().addLast(new TimeServerHandler());
    }

}