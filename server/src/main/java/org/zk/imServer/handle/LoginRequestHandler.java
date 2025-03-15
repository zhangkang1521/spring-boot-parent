package org.zk.imServer.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.common.concurrent.CallbackTask;
import org.zk.common.concurrent.CallbackTaskScheduler;
import org.zk.imServer.process.LoginProcess;
import org.zk.imServer.session.ServerSession;

/**
 * @author zhangkang
 * @date 2024/12/15 20:25
 */
@Component
@Slf4j
@ChannelHandler.Sharable // 必须是共享的，否则多次添加移除会报错
public class LoginRequestHandler extends ChannelInboundHandlerAdapter {

    @Autowired
    private LoginProcess loginProcess;

    @Autowired
    private ChatRedirectHandler chatRedirectHandler;

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        if (ProtoMsg.HeadType.LOGIN_REQUEST != message.getType()) {
            super.channelRead(ctx, msg);
            return;
        }

        ServerSession session = new ServerSession(ctx.channel());

        // 登录校验异步处理
        CallbackTaskScheduler.add(new CallbackTask<Boolean>() {

            public Boolean execute() throws Exception {
                return loginProcess.process(session, message);
            }

            public void onBack(Boolean r) {
                if (r) {
                    log.info("登录成功，移除login处理器，添加聊天处理器，心跳处理器");
                    ctx.pipeline().addAfter("login", "chat", chatRedirectHandler);
                    ctx.pipeline().addAfter("login", "heartBeat", new HeartBeatServerHandler());
                    ctx.pipeline().remove("login");
                }
            }

            public void onException(Throwable t) {
                log.error("登录处理失败", t);
            }
        });
    }
}
