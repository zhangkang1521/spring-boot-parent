package org.zk.imClient.sender;

import org.springframework.stereotype.Component;
import org.zk.common.beans.User;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.command.ChatCommand;
import org.zk.imClient.protoConverter.ChatMsgConvert;
import org.zk.imClient.protoConverter.LoginMsgConvert;
import org.zk.imClient.session.ClientSession;

/**
 * @author zhangkang
 * @date 2024/12/14 20:00
 */
@Component
public class ChatSender {



    public void send(ClientSession clientSession, ChatCommand command) {
        User user = clientSession.getUser();
        ProtoMsg.Message msg = ChatMsgConvert.build(user.getUid(), command.getToUid(), command.getContent());
        clientSession.getChannel().writeAndFlush(msg);
    }
}
