package com.example.nettywebsocket;

import com.example.nettywebsocket.socket.IntUtils;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class NettyWebsocketApplicationTests {

    @Test
    void contextLoads() {
        int order= IntUtils.getRandomNumberInRange(0,1);
        log.error("message 处理异常， msg: {}", order);
    }

}
