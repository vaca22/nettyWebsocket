package com.example.nettywebsocket;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Slf4j
class NettyWebsocketApplicationTests {

    @Test
    void contextLoads() {
        int order=com.example.nettywebsocket.IntUtils.getRandomNumberInRange(0,1);
        log.error("message 处理异常， msg: {}", order);
    }

}
