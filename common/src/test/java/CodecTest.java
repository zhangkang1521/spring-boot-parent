import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.embedded.EmbeddedChannel;
import org.junit.Test;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.common.codec.SimpleProtoMsgDecoder;
import org.zk.common.codec.SimpleProtoMsgEncoder;

/**
 * @author zhangkang
 * @date 2024/12/15 19:31
 */
public class CodecTest {

    @Test
    public void test1() {
        ProtoMsg.Message.Builder mb =
                ProtoMsg.Message.newBuilder()
                        .setType(ProtoMsg.HeadType.LOGIN_REQUEST)
                        .setSessionId("w352323rfes")
                        .setSequence(-1);

        ProtoMsg.LoginRequest.Builder loginRequestBuilder =
                ProtoMsg.LoginRequest.newBuilder()
                        .setPassword("123456")
                        .setUid("100");

        ProtoMsg.Message requestMsg = mb
                .setLoginRequest(loginRequestBuilder)
                .build();

        EmbeddedChannel embeddedChannel = new EmbeddedChannel(new ChannelInitializer<Channel>() {
            @Override
            protected void initChannel(Channel ch) throws Exception {
                ch.pipeline().addLast(new SimpleProtoMsgDecoder());
                ch.pipeline().addLast(new SimpleProtoMsgEncoder());
            }
        });
        // embeddedChannel.writeInbound(requestMsg);
        // embeddedChannel.writeOutbound(requestMsg);
    }
}
