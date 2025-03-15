package org.zk.imClient.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.util.concurrent.GenericFutureListener;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zk.common.codec.SimpleProtoMsgDecoder;
import org.zk.common.codec.SimpleProtoMsgEncoder;
import org.zk.imClient.handler.ClientExceptionHandler;
import org.zk.imClient.handler.LoginResponseHandler;

/**
 * @author zhangkang
 * @date 2024/12/14 15:46
 */
@Component
@Slf4j
public class ChatNettyClient {

    private String host = "127.0.0.1";

    private int port = 8888;

    private GenericFutureListener<ChannelFuture> connectedListener;

    @Autowired
    private LoginResponseHandler loginResponseHandler;

    @Autowired
    private ClientExceptionHandler clientExceptionHandler;

    public void setConnectedListener(GenericFutureListener<ChannelFuture> connectedListener) {
        this.connectedListener = connectedListener;
    }

    public void connect() {
        Bootstrap bootstrap = new Bootstrap();
        EventLoopGroup group = new NioEventLoopGroup();
        try {
            bootstrap.group(group);
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.handler(new ChannelInitializer<SocketChannel>() {
                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleProtoMsgDecoder());
                    ch.pipeline().addLast(new SimpleProtoMsgEncoder());
                    ch.pipeline().addLast("login", loginResponseHandler);
                    ch.pipeline().addLast("exception", clientExceptionHandler);
                }
            });

            log.info("发起连接 {} {}", host, port);
            ChannelFuture channelFuture = bootstrap.connect(host, port);

            channelFuture.addListener(connectedListener);

            // 等待通道关闭
//            channelFuture.channel().closeFuture().sync();
//            log.info("客户端退出");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
           // group.shutdownGracefully();
        }
    }
}
