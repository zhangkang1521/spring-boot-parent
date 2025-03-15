package org.zk.imClient;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.zk.imClient.client.ChatNettyClient;
import org.zk.imClient.client.CommandController;

/**
 * 客户端启动类
 */
@SpringBootApplication
public class ClientApplication {

    public static void main(String[] args) {
        ApplicationContext applicationContext = SpringApplication.run(ClientApplication.class, args);
        CommandController commandController = applicationContext.getBean(CommandController.class);
        commandController.initCommand();
        commandController.start();
    }
}
