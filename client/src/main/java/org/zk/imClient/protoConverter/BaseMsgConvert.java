package org.zk.imClient.protoConverter;

import org.zk.common.beans.msg.ProtoMsg;
import org.zk.imClient.session.ClientSession;

/**
 * @author zhangkang
 * @date 2024/12/15 18:52
 */
public class BaseMsgConvert {

    private ProtoMsg.HeadType headType;


    private ClientSession clientSession;

    public BaseMsgConvert(ProtoMsg.HeadType headType, ClientSession clientSession) {
        this.headType = headType;
        this.clientSession = clientSession;
    }


    public ProtoMsg.Message.Builder getOuterBuilder(long seqId) {
        ProtoMsg.Message.Builder mb =
                ProtoMsg.Message.newBuilder()
                        .setType(headType)
                        .setSessionId(clientSession.getSessionId())
                        .setSequence(seqId);
        return mb;
    }
}
