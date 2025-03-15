package org.zk.imServer.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imServer.protoConverter.ChatResponseConvert;
import org.zk.imServer.session.ServerSession;
import org.zk.imServer.session.SessionMap;

/**
 * @author zhangkang
 * @date 2024/12/17 16:05
 */
@Component
@Slf4j
@ChannelHandler.Sharable
public class ChatRedirectHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        if (ProtoMsg.HeadType.CHAT_REQUEST != message.getType()) {
            super.channelRead(ctx, msg);
            return;
        }

        // TODO 转发fromUid发送成功还是失败
        ProtoMsg.ChatRequest chatRequest = message.getChatRequest();
        String toUid = chatRequest.getToUid();
        ServerSession session = SessionMap.getServerSession(toUid);
        if (session == null) {
            log.warn("用户 {} 不在线", toUid);
            return;
        }

        log.info("服务端转发消息 from: {} to:{} content:{}", chatRequest.getFromUid(), toUid, chatRequest.getContent());
        ProtoMsg.Message respMessage = ChatResponseConvert.build(message);
        session.getChannel().writeAndFlush(respMessage);

    }
}
