package org.zk.imClient.protoConverter;

import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.session.ClientSession;

/**
 * @author zhangkang
 * @date 2024/12/18 16:28
 */
public class ChatMsgConvert {

    public static ProtoMsg.Message build(String from, String to, String content) {
        ProtoMsg.ChatRequest chatRequest = ProtoMsg.ChatRequest.newBuilder()
                .setFromUid(from)
                .setToUid(to)
                .setContent(content)
                .build();

        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.CHAT_REQUEST)
                .setSequence(System.currentTimeMillis())
                .setChatRequest(chatRequest);
        return mb.build();
    }
}
