package com.oven.netty.study.time.demo2;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * Netty时间服务器客户端
 */
public class TimeClient {

    public void connect(int port, String host) throws Exception {
        // 配置客户端的NIO线程组
        EventLoopGroup clientGroup = new NioEventLoopGroup();
        try {
            // Client辅助启动类
            Bootstrap bootstrap = new Bootstrap();
            // 配置bootstrap
            bootstrap.group(clientGroup)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(new ChannelInitializer<SocketChannel>() {
                        /**
                         * 创建NioSocketChannel成功之后，进行初始化时，
                         * 将ChannelHandler设置到ChannelPipeline中，
                         * 同样，用于处理网络I/O事件
                         * @param ch
                         */
                        @Override
                        protected void initChannel(SocketChannel ch) {
                            ch.pipeline().addLast(new TimeClientHandler());
                        }
                    });
            // 发起异步连接操作  同步方法待成功
            ChannelFuture future = bootstrap.connect(host, port).sync();
            // 等待客户端链路关闭
            future.channel().closeFuture().sync();
        } finally {
            // 优雅退出，释放NIO线程组
            clientGroup.shutdownGracefully();
        }


    }
}
