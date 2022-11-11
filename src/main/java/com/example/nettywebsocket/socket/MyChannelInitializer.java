package com.example.nettywebsocket.socket;



import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class MyChannelInitializer extends ChannelInitializer<SocketChannel> {

    @Override
    protected void initChannel(SocketChannel socketChannel) throws Exception {
        log.info("收到新的客户端连接: {}", socketChannel.toString());
        socketChannel.pipeline().addLast("IdleState",new IdleStateHandler(10,0,0, TimeUnit.SECONDS));
        socketChannel.pipeline().addLast(new HttpServerCodec());
        socketChannel.pipeline().addLast(new ChunkedWriteHandler());
        socketChannel.pipeline().addLast(new HttpObjectAggregator(8192));
        socketChannel.pipeline().addLast(new MyWebSocketHandler());
        socketChannel.pipeline().addLast(new WebSocketServerProtocolHandler("/ws", "WebSocket", true, 65536 * 10));
        socketChannel.pipeline().addLast("IdleStateEventHandler",new IdleStateEventHandler());
    }
}