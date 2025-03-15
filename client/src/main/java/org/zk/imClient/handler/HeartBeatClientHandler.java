package org.zk.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zk.common.GlobalInstant;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.protoConverter.HeartBeatMsgConvert;
import org.zk.imClient.session.ClientSession;

import java.util.concurrent.TimeUnit;

/**
 * @author zhangkang
 * @date 2024/12/19 9:40
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class HeartBeatClientHandler extends ChannelInboundHandlerAdapter {

    private volatile int count = 0;

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        // 必须手动调用
        log.info("开始发送心跳包");
        ClientSession clientSession = ctx.channel().attr(ClientSession.SESSION_KEY).get();

        ProtoMsg.Message msg = HeartBeatMsgConvert.build(clientSession.getUser().getUid());
        sendHeartBeat(ctx, msg);
    }

    private void sendHeartBeat(ChannelHandlerContext ctx, ProtoMsg.Message msg) {
        ctx.executor().schedule(() -> {
            if (ctx.channel().isActive()) {
                log.info("发送心跳包");
                ctx.writeAndFlush(msg);
                sendHeartBeat(ctx, msg);
            } else {
                log.warn("channel is inActive");
            }
        }, GlobalInstant.HEART_BEAT_INTERVAL, TimeUnit.SECONDS);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        if (ProtoMsg.HeadType.HEART_BEAT != message.getType()) {
            super.channelRead(ctx, msg);
            return;
        }

        log.info("收到服务端心跳回写 {}", message.getHeartBeatRequest().getUid());
    }
}
