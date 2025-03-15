package org.zk.imClient.sender;

import org.springframework.stereotype.Component;
import org.zk.common.beans.User;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.protoConverter.LoginMsgConvert;
import org.zk.imClient.session.ClientSession;

/**
 * @author zhangkang
 * @date 2024/12/14 20:01
 */
@Component
public class LoginSender {

    private User user;

    private ClientSession clientSession;

    public void send() {
        ProtoMsg.Message msg = LoginMsgConvert.build(user, clientSession);
        clientSession.getChannel().writeAndFlush(msg);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public void setClientSession(ClientSession clientSession) {
        this.clientSession = clientSession;
    }
}
