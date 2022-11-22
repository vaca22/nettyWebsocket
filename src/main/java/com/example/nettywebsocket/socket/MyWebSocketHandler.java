package com.example.nettywebsocket.socket;


import com.example.nettywebsocket.sql.ChessMapper;
import com.example.nettywebsocket.sql.ChessUser;
import org.json.JSONArray;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static com.example.nettywebsocket.socket.SocketConst.ORDER;
import static com.example.nettywebsocket.sql.ChessDatabase.chessMapper;

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
        Channel incomming = ctx.channel();
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
        updateUser();

    }

    private static Map getUrlParams(String url) {
        Map<String, String> map = new HashMap<>();
        url = url.replace("?", ";");
        if (!url.contains(";")) {
            return map;
        }
        if (url.split(";").length > 0) {
            String[] arr = url.split(";")[1].split("&");
            for (String s : arr) {
                String key = s.split("=")[0];
                String value = s.split("=")[1];
                map.put(key, value);
            }
            return map;

        } else {
            return map;
        }
    }


    public void broadcast(String info) {
        new Thread(() -> {
            for (Channel channel : channelGroup) {
                channel.writeAndFlush(new TextWebSocketFrame(info));
            }
        }).start();

    }

    public void updateUser() {
        JSONObject sendInfo = new JSONObject();
        sendInfo.put("id", "dada");
        sendInfo.put("toid", "dada");
        sendInfo.put("action", "update");



        Set<String> userSet = channelMap.keySet();

        JSONArray users = new JSONArray();
        for (String key : userSet) {
            users.put(chessMapper.findById(key).toJson());
        }
        sendInfo.put("info", users);
        broadcast(sendInfo.toString());
    }


    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (null != msg && msg instanceof FullHttpRequest) {
            FullHttpRequest request = (FullHttpRequest) msg;
            String uri = request.uri();

            Map paramMap = getUrlParams(uri);
            String phone = (String) paramMap.get("phone");
            if (phone != null) {
                if (phone.length() > 0) {
                    channelMap.put(phone, ctx.channel());
                    updateUser();;
                }
            }

            //如果url包含参数，需要处理
            if (uri.contains("?")) {
                String newUri = uri.substring(0, uri.indexOf("?"));
                request.setUri(newUri);
            }
        }
        super.channelRead(ctx, msg);
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, TextWebSocketFrame msg) throws Exception {
        log.info(msg.text());
        JSONObject msgObject = new JSONObject(msg.text());
        String fromId = msgObject.getString("id");
        String toId = msgObject.getString("toid");
        String action = msgObject.getString("action");


        try {
            Channel b = channelMap.get(toId);
            Channel me = channelMap.get(fromId);
            if (b != null) {
                if(action.equals(SocketConst.ACCEPT)){
                    int order= IntUtils.getRandomNumberInRange(0,1);
                    msgObject.put(ORDER,order);
                    b.writeAndFlush(new TextWebSocketFrame(msgObject.toString()));
                    msgObject.put(ORDER,1-order);
                    me.writeAndFlush(new TextWebSocketFrame(msgObject.toString()));
                }else{
                    b.writeAndFlush(new TextWebSocketFrame(msgObject.toString()));
                }

            } else {
                Channel b2 = channelMap.get(fromId);
                msgObject.put("action", "offline");
                b2.writeAndFlush(new TextWebSocketFrame(msgObject.toString()));
            }
        } catch (Exception e) {
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