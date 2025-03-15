package org.zk.imServer.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.UnpooledByteBufAllocator;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.protobuf.ProtobufDecoder;
import io.netty.handler.codec.protobuf.ProtobufEncoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32FrameDecoder;
import io.netty.handler.codec.protobuf.ProtobufVarint32LengthFieldPrepender;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.zk.common.codec.SimpleProtoMsgDecoder;
import org.zk.common.codec.SimpleProtoMsgEncoder;
import org.zk.imServer.handle.LoginRequestHandler;
import org.zk.imServer.handle.ServerExceptionHandler;

/**
 * 启动netty服务器
 *
 * @author zhangkang
 * @date 2024/12/14 15:35
 */
@Component
@Slf4j
public class ChatServer {

    private int port = 8888;

    @Autowired
    private LoginRequestHandler loginRequestHandler;

    @Autowired
    private ServerExceptionHandler serverExceptionHandler;

    @SneakyThrows
    public void start() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            serverBootstrap.group(bossGroup, workerGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>() {

                @Override
                protected void initChannel(SocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new SimpleProtoMsgDecoder());
                    ch.pipeline().addLast(new SimpleProtoMsgEncoder());
                    ch.pipeline().addLast("login", loginRequestHandler);
                    ch.pipeline().addLast("exception", serverExceptionHandler);
                }
            });
            ChannelFuture channelFuture = serverBootstrap.bind(port).sync();
            log.info("服务器启动成功 {}", port);
            channelFuture.channel().closeFuture().sync();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
