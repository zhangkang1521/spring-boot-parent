package org.zk.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zk.imClient.client.CommandController;

/**
 * @author zhangkang
 * @date 2024/12/19 15:39
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ClientExceptionHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private CommandController commandController;

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("连接服务器异常，尝试重新连接");
        commandController.setConnectFlag(false);
        commandController.startConnectServer();
    }
}
