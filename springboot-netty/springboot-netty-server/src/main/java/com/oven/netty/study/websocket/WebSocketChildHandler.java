package com.oven.netty.study.websocket;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.stream.ChunkedWriteHandler;

/**
 * webSocketChildHandler
 */
public class WebSocketChildHandler extends ChannelInitializer<SocketChannel> {

    /**
     * 初始化Channel
     *
     * @param socketChannel socketChannel
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline pipeline = socketChannel.pipeline();
        // 将请求与应答消息编码或者解码为HTTP消息
        pipeline.addLast("http-codec", new HttpServerCodec());
        // 将http消息的多个部分组合成一条完整的HTTP消息
        pipeline.addLast("aggregator", new HttpObjectAggregator(65536));
        // 向客户端发送HTML5文件。主要用于支持浏览器和服务端进行WebSocket通信
        pipeline.addLast("http-chunked", new ChunkedWriteHandler());
        // 服务端Handler
        pipeline.addLast("handler", new WebSocketServerHandler());
    }

}
