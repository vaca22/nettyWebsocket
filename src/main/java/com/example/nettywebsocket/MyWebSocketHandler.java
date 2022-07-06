package com.example.nettywebsocket;


import org.json.JSONObject;

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
        channelGroup.remove(ctx.channel());
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
            String phone= (String) paramMap.get("phone");
            channelMap.put(phone,ctx.channel());
            //如果url包含参数，需要处理
            if(uri.contains("?")){
                String newUri=uri.substring(0,uri.indexOf("?"));
                request.setUri(newUri);
            }
        }
        super.channelRead(ctx,msg);
    }
    public  static  int roomNum=100000;

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info(msg.text());
        JSONObject a=new JSONObject(msg.text());
        String fromId=a.getString("id");
        String toId=a.getString("toid");
        String action=a.getString("action");
//        if(action.equals("call")){
//           a.put("roomNum",""+roomNum);
//           roomNum++;
//           if(roomNum>10000000){
//               roomNum=10000;
//           }
//        }


        try {
            Channel b=channelMap.get(toId);
            if(b!=null){
                b.writeAndFlush(new TextWebSocketFrame(a.toString()));
            }else{
                Channel b2=channelMap.get(fromId);
                a.put("action","offline");
                b2.writeAndFlush(new TextWebSocketFrame(a.toString()));
            }
        }catch (Exception e){
            log.error("message 处理异常， msg: {}, date: {}", msg, e);
        }

    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        Channel incoming = ctx.channel();
        log.error("ChatClient:" + incoming.remoteAddress() + "异常", cause);
        ctx.close();
    }



}