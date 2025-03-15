package org.zk.imClient.session;

import io.netty.channel.Channel;
import io.netty.util.AttributeKey;
import org.zk.common.beans.User;

import java.util.UUID;

/**
 * @author zhangkang
 * @date 2024/12/14 19:24
 */
public class ClientSession {

    private Channel channel;

    private String sessionId;

    /**
     * 是否登录，登录成功置为true
     */
    private boolean isLogin = false;

    /**
     * 用户，登录时设值
     */
    private User user;

    public static final AttributeKey<ClientSession> SESSION_KEY = AttributeKey.valueOf("clientSession");

    public ClientSession(Channel channel) {
        this.channel = channel;
        // 将session绑定到channel，以便入站时通过channel获取session
        this.channel.attr(SESSION_KEY).set(this);
        this.sessionId = UUID.randomUUID().toString();
    }

    public Channel getChannel() {
        return channel;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setLogin(boolean login) {
        isLogin = login;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
