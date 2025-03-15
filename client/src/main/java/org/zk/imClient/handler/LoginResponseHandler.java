package org.zk.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.session.ClientSession;

/**
 * @author zhangkang
 * @date 2024/12/17 15:25
 */
@Slf4j
@Component
@ChannelHandler.Sharable
public class LoginResponseHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private ChatResponseHandler chatResponseHandler;

    @Autowired
    private HeartBeatClientHandler heartBeatClientHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        if (ProtoMsg.HeadType.LOGIN_RESPONSE != message.getType()) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.LoginResponse loginResponse = message.getLoginResponse();
        ClientSession clientSession = ctx.channel().attr(ClientSession.SESSION_KEY).get();
        if (loginResponse.getResult()) {
            clientSession.setLogin(true);
            log.info("登录成功，移除登录处理器,添加聊天处理器，心跳处理器");
            ctx.pipeline().addAfter("login", "chat", chatResponseHandler);
            ctx.pipeline().addAfter("login", "heartBeat", heartBeatClientHandler);
            ctx.pipeline().remove(this);
            // 触发心跳发送
            heartBeatClientHandler.channelActive(ctx);
        } else {
            log.info("登录失败");
        }
    }
}
