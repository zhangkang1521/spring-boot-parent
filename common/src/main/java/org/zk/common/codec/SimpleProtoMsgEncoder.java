package org.zk.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;
import lombok.extern.slf4j.Slf4j;
import org.zk.common.ProtoInstant;
import org.zk.common.beans.msg.ProtoMsg;

/**
 * ProtoMsg.Message编码
 * @author zhangkang
 * @date 2024/12/15 19:12
 */
@Slf4j
public class SimpleProtoMsgEncoder extends MessageToByteEncoder<ProtoMsg.Message> {


    @Override
    protected void encode(ChannelHandlerContext ctx, ProtoMsg.Message msg, ByteBuf out) throws Exception {
        //log.info("编码 {}", msg);
        out.writeShort(ProtoInstant.MAGIC_CODE);
        out.writeShort(ProtoInstant.VERSION);
        byte[] bytes = msg.toByteArray();
        out.writeInt(bytes.length);
        out.writeBytes(bytes);
    }
}
