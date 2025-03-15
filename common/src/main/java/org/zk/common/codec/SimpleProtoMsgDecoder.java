package org.zk.common.codec;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import lombok.extern.slf4j.Slf4j;
import org.zk.common.ProtoInstant;
import org.zk.common.beans.msg.ProtoMsg;

import java.util.List;

/**
 * ProtoMsg.Message解码
 * @author zhangkang
 * @date 2024/12/15 19:18
 */
@Slf4j
public class SimpleProtoMsgDecoder extends ByteToMessageDecoder {

    public static final int HEADER_LEN = 8;

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        in.markReaderIndex();

        int len = in.readableBytes();
        if (len < HEADER_LEN) {
            return;
        }
        int magicCode = in.readShort();
        if (magicCode != ProtoInstant.MAGIC_CODE) {
            log.error("magicCode wrong");
            return;
        }
        int version = in.readShort();
        if (version != ProtoInstant.VERSION) {
            log.error("version wrong");
            return;
        }
        int dataLen = in.readInt();
        if (dataLen < in.readableBytes()) {
            in.resetReaderIndex();
            return;
        }

        byte[] bytes = new byte[dataLen];
        in.readBytes(bytes);
        ProtoMsg.Message msg = ProtoMsg.Message.parseFrom(bytes);
        //log.info("解码 {}", msg);
        out.add(msg);
    }
}
