package com.example.nettywebsocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@ServletComponentScan("com")
@EntityScan(basePackages = {"com"})
@EnableScheduling
@EnableAsync
public class NettyWebsocketApplication {

    public static void main(String[] args) {
        SpringApplication.run(NettyWebsocketApplication.class, args);

        // 启动netty服务器
        try {
            new NettyServer(3003).start();
        } catch (Exception e) {
            System.out.println("NettyServerError:" + e.getMessage());
        }
    }
}
