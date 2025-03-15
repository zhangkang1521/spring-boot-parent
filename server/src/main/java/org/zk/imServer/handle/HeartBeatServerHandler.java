package org.zk.imServer.handle;

import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;
import org.zk.common.GlobalInstant;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imServer.protoConverter.ChatResponseConvert;
import org.zk.imServer.session.ServerSession;
import org.zk.imServer.session.SessionMap;

/**
 * @author zhangkang
 * @date 2024/12/18 17:27
 */
@Slf4j
public class HeartBeatServerHandler extends IdleStateHandler {
    public HeartBeatServerHandler() {
        super(10 * GlobalInstant.HEART_BEAT_INTERVAL, 0, 0);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }

        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        if (ProtoMsg.HeadType.HEART_BEAT == message.getType()) {
            log.info("收到心跳包并回写 {}", message.getHeartBeatRequest());
            ctx.writeAndFlush(message);
        }

        // 这行不能漏，否则检测空闲链接有问题
        super.channelRead(ctx, msg);

    }

    @Override
    protected void channelIdle(ChannelHandlerContext ctx, IdleStateEvent evt) throws Exception {
        log.info("通道空闲自动关闭 {} {}", evt.state(), evt.isFirst());
        ServerSession.close(ctx);
    }
}
