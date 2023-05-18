package com.example.nettywebsocket;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan("com")
@EntityScan(basePackages = {"com"})
@EnableScheduling
@EnableAsync
@Slf4j
public class NettyWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyWebsocketApplication.class, args);

        // 启动netty服务器
        try {
            new NettyServer(3003).start();
        } catch (Exception e) {
            log.error("NettyServerError:" + e.getMessage());
        }
    }
}
