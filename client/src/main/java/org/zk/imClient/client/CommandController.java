package org.zk.imClient.client;

import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoop;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zk.common.concurrent.FutureTaskScheduler;
import org.zk.imClient.command.ChatCommand;
import org.zk.imClient.command.ICommand;
import org.zk.imClient.command.LoginCommand;
import org.zk.imClient.sender.ChatSender;
import org.zk.imClient.sender.LoginSender;
import org.zk.imClient.session.ClientSession;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

/**
 * @author zhangkang
 * @date 2024/12/14 16:09
 */
@Component
@Slf4j
public class CommandController {

    private boolean connectFlag = false;

    @Autowired
    private ChatNettyClient chatNettyClient;

    private ClientSession clientSession;

    private Map<String, ICommand> commandMap = new HashMap<>();

    @Autowired
    private List<ICommand> commands;

    @Autowired
    private LoginSender loginSender;

    @Autowired
    private ChatSender chatSender;


    public void initCommand() {
        for (ICommand command : commands) {
            commandMap.put(command.getKey(), command);
        }
    }

    /**
     * 连接回调
     */
    GenericFutureListener<ChannelFuture> connectedListener = (ChannelFuture f) ->
    {
        final EventLoop eventLoop
                = f.channel().eventLoop();
        if (!f.isSuccess()) {
            log.info("连接失败!在10s之后准备尝试重连!");
            eventLoop.schedule(() -> chatNettyClient.connect(),
                    10, TimeUnit.SECONDS);

            connectFlag = false;
        } else {
            connectFlag = true;

            log.info("连接服务器成功，唤醒主线程");
            // TODO zk 添加会话
            Channel channel = f.channel();
//
//            // 创建会话
             this.clientSession = new ClientSession(channel);
//            session.setConnected(true);
//            channel.closeFuture().addListener(closeListener);

            //唤醒用户线程
            notifyThread();
        }

    };

    public void startConnectServer() {
        log.info("往线程池添加连接任务");
        FutureTaskScheduler.add(() -> {
            chatNettyClient.setConnectedListener(connectedListener);
            chatNettyClient.connect();
        });
    }

    public void start() {
        while (connectFlag == false) {
            // 添加连接任务
            startConnectServer();
            log.info("等待连接成功，连接成功回调会唤醒主线程");
            this.waitConnect();
        }

        while(this.clientSession != null) {
            Scanner scanner = new Scanner(System.in);
            log.info("请选择菜单 1.登录 2.聊天");
            String key = scanner.nextLine();
            ICommand command = commandMap.get(key);
            switch (key) {
                case "1":
                    command.execute(scanner);
                    login((LoginCommand) command);
                    break;
                case "2":
                    command.execute(scanner);
                    chat((ChatCommand) command);
                    break;
            }
        }
    }

    private void login(LoginCommand command) {
        loginSender.setClientSession(clientSession);
        loginSender.setUser(command.getUser());
        loginSender.send();

        clientSession.setUser(command.getUser());
    }

    private void chat(ChatCommand command) {
        chatSender.send(clientSession, command);
    }



    public synchronized void notifyThread() {
        this.notify();
    }

    @SneakyThrows
    public synchronized void waitConnect() {
        this.wait();
    }

    public void setConnectFlag(boolean connectFlag) {
        this.connectFlag = connectFlag;
    }
}
