package org.zk.imServer.protoConverter;

import org.zk.common.beans.msg.ProtoMsg;

/**
 * @author zhangkang
 * @date 2024/12/17 15:04
 */
public class ChatResponseConvert {

    public static ProtoMsg.Message build(ProtoMsg.Message message) {
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.CHAT_RESPONSE)
                .setSequence(message.getSequence())
                .setSessionId("123");

        ProtoMsg.ChatResponse.Builder lb = ProtoMsg.ChatResponse.newBuilder()
                .setContent(message.getChatRequest().getContent())
                .setFromUid(message.getChatRequest().getFromUid())
                .setToUid(message.getChatRequest().getToUid());

       mb.setChatResponse(lb.build());
       return mb.build();
    }
}
