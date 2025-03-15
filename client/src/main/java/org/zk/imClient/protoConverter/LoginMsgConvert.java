package org.zk.imClient.protoConverter;

import org.zk.common.beans.User;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.session.ClientSession;

/**
 * @author zhangkang
 * @date 2024/12/15 18:51
 */
public class LoginMsgConvert extends BaseMsgConvert {

    private User user;


    public LoginMsgConvert(User user, ClientSession clientSession) {
        super(ProtoMsg.HeadType.LOGIN_REQUEST, clientSession);
        this.user = user;
    }

    public ProtoMsg.Message build() {

        ProtoMsg.Message.Builder outerBuilder = getOuterBuilder(-1);


        ProtoMsg.LoginRequest.Builder loginRequestBuilder =
                ProtoMsg.LoginRequest.newBuilder()
                        .setPassword(user.getPassword())
                        .setUid(user.getUid());

        ProtoMsg.Message requestMsg = outerBuilder
                .setLoginRequest(loginRequestBuilder)
                .build();

        return requestMsg;
    }

    public static ProtoMsg.Message build(User user, ClientSession clientSession) {
        LoginMsgConvert loginMsgConvert = new LoginMsgConvert(user, clientSession);
        return loginMsgConvert.build();
    }
}
