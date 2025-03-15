package org.zk.imServer.handle;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.server.Session;
import org.springframework.stereotype.Component;
import org.zk.imServer.session.ServerSession;

/**
 * @author zhangkang
 * @date 2024/12/19 15:30
 */
@Component
@ChannelHandler.Sharable
@Slf4j
public class ServerExceptionHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.info("客户端连接异常，关闭连接 {}", ctx.channel().remoteAddress());
        ServerSession.close(ctx);
        // super.exceptionCaught(ctx, cause);
    }
}
