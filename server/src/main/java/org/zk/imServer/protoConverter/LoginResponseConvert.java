package org.zk.imServer.protoConverter;

import org.zk.common.beans.msg.ProtoMsg;

/**
 * @author zhangkang
 * @date 2024/12/17 15:04
 */
public class LoginResponseConvert {

    public static ProtoMsg.Message build(long seqId, boolean success, String reason) {
        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.LOGIN_RESPONSE)
                .setSequence(seqId)
                .setSessionId("123");

        ProtoMsg.LoginResponse.Builder lb = ProtoMsg.LoginResponse.newBuilder()
                .setResult(success)
                .setCode(success ? 200 : 500)
                .setInfo(reason);

       mb.setLoginResponse(lb.build());
       return mb.build();
    }
}
