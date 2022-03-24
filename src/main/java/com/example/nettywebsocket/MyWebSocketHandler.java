package com.example.nettywebsocket;


import com.alibaba.fastjson.JSON;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.util.concurrent.GlobalEventExecutor;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 自定义服务器端处理handler，继承SimpleChannelInboundHandler，处理WebSocket 连接数据
 */
@Slf4j
public class MyWebSocketHandler extends SimpleChannelInboundHandler<TextWebSocketFrame> {

    public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    // 用户id=>channel示例
    // 可以通过用户的唯一标识保存用户的channel
    // 这样就可以发送给指定的用户
    public static ConcurrentHashMap<String, Channel> channelMap = new ConcurrentHashMap<>();

    /**
     * 每当服务端收到新的客户端连接时,客户端的channel存入ChannelGroup列表中,并通知列表中其他客户端channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        // 获取连接的channel
        log.info("netty客户端连接删除成功! fuckyou");

        Channel incomming = ctx.channel();
        //通知所有已经连接到服务器的客户端，有一个新的通道加入
        /*for(Channel channel:channelGroup){
            channel.writeAndFlush("[SERVER]-"+incomming.remoteAddress()+"加入\n");
        }*/
        channelGroup.add(incomming);
    }

    /**
     * 每当服务端断开客户端连接时,客户端的channel从ChannelGroup中移除,并通知列表中其他客户端channel
     *
     * @param ctx
     * @throws Exception
     */
    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        //获取连接的channel
        /*Channel incomming = ctx.channel();
        for(Channel channel:channelGroup){
            channel.writeAndFlush("[SERVER]-"+incomming.remoteAddress()+"离开\n");
        }*/
        //从服务端的channelGroup中移除当前离开的客户端
        channelGroup.remove(ctx.channel());

        //从服务端的channelMap中移除当前离开的客户端
        Collection<Channel> col = channelMap.values();
        while (true == col.contains(ctx.channel())) {
            col.remove(ctx.channel());
            log.info("netty客户端连接删除成功!");
        }

    }

    private static Map getUrlParams(String url){
        Map<String,String> map = new HashMap<>();
        url = url.replace("?",";");
        if (!url.contains(";")){
            return map;
        }
        if (url.split(";").length > 0){
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr){
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key,value);
            }
            return  map;

        }else{
            return map;
        }
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            Map paramMap=getUrlParams(uri);
            System.out.println("接收到的参数是："+ JSON.toJSONString(paramMap));
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                System.out.println(newUri);
                request.setUri(newUri);
            }
        }
        super.channelRead(ctx,msg);
    }
    /**
     * 每当从服务端读到客户端写入信息时,将信息转发给其他客户端的Channel.
     *
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info("netty客户端连接删除成功2333333333333333333!");
        log.info("netty客户端收到服务器数据, 客户端地址: {}, msg: {}", ctx.channel().remoteAddress(), msg.text());
        String date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        //消息处理类
        message(ctx, msg.text(), date);

        //channelGroup.writeAndFlush( new TextWebSocketFrame(msg.text()));
    }

    /**
     * 当服务端的IO 抛出异常时被调用
     *
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        log.error("SimpleChatClient:" + incoming.remoteAddress() + "异常", cause);
        //异常出现就关闭连接
        ctx.close();
    }

    //消息处理类
    public void message(ChannelHandlerContext ctx, String msg, String date) {
        try {
            // 消息转发给在线的其他用户
            Channel channel = ctx.channel();
            for (Channel tmpChannel : channelGroup) {
                if (!tmpChannel.equals(channel)) {
                  //  String sendedMsg = date + "：" + msg;
                    String sendedMsg = msg;
                    log.info("服务器转发消息,客户端地址: {}, msg: {}", ctx.channel().remoteAddress(), sendedMsg);
                    tmpChannel.writeAndFlush(new TextWebSocketFrame(sendedMsg));
                }
            }
        } catch (Exception e) {
            log.error("message 处理异常， msg: {}, date: {}", msg, date, e);
        }
    }
}