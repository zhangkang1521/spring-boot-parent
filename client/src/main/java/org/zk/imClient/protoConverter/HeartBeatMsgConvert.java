package org.zk.imClient.protoConverter;

import org.zk.common.beans.msg.ProtoMsg;

/**
 * @author zhangkang
 * @date 2024/12/18 16:28
 */
public class HeartBeatMsgConvert {

    public static ProtoMsg.Message build(String uid) {
        ProtoMsg.HeartBeatRequest heartBeatRequest = ProtoMsg.HeartBeatRequest.newBuilder()
                .setUid(uid)
                .build();

        ProtoMsg.Message.Builder mb = ProtoMsg.Message.newBuilder()
                .setType(ProtoMsg.HeadType.HEART_BEAT)
                .setSequence(System.currentTimeMillis())
                .setHeartBeatRequest(heartBeatRequest);
        return mb.build();
    }
}
