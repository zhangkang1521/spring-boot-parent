package org.zk.imServer.session;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.util.AttributeKey;
import lombok.extern.slf4j.Slf4j;
import org.zk.common.beans.User;


/**
 * @author zhangkang
 * @date 2024/12/15 20:34
 */
@Slf4j
public class ServerSession {

    private Channel channel;

    /**
     * 用户信息，登录成功设置
     */
    private User user;

    public static final AttributeKey<ServerSession> SERVER_SESSION = AttributeKey.newInstance("session");

    public ServerSession(Channel channel) {
        this.channel = channel;
        channel.attr(SERVER_SESSION).set(this);
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    public Channel getChannel() {
        return channel;
    }

    public static void close(ChannelHandlerContext ctx) {
        ServerSession session = ctx.channel().attr(ServerSession.SERVER_SESSION).get();
        if (session != null && session.getUser() != null) {
            log.info("用户{}下线", session.getUser().getUid());
            SessionMap.removeSession(session.getUser().getUid());
        }
        log.info("关闭客户端连接 {}", ctx.channel().remoteAddress());
        ctx.channel().close();
    }
}
