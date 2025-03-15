package org.zk.imClient.handler;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
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
public class ChatResponseHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        if (!(msg instanceof ProtoMsg.Message)) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.Message message = (ProtoMsg.Message) msg;
        if (ProtoMsg.HeadType.CHAT_RESPONSE != message.getType()) {
            super.channelRead(ctx, msg);
            return;
        }
        ProtoMsg.ChatResponse chatResponse = message.getChatResponse();
        log.info("收到聊天消息 from:{} content:{}", chatResponse.getFromUid(), chatResponse.getContent());
    }
}
