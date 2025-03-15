package org.zk.imServer;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.zk.imServer.server.ChatServer;

@SpringBootApplication
public class ServerApplication {
    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ServerApplication.class);
        ChatServer chatServer = applicationContext.getBean(ChatServer.class);
        chatServer.start();
    }
}
