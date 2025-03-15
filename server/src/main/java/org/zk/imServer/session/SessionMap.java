package org.zk.imServer.session;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhangkang
 * @date 2024/12/17 15:52
 */
@Slf4j
public class SessionMap {

    /**
     * 服务端所有登录用户 uid -> ServerSession
     */
    private static Map<String, ServerSession> serverSessionMap = new ConcurrentHashMap<>();


    public static ServerSession getServerSession(String uid) {
        return serverSessionMap.get(uid);
    }

    public static void putServerSession(String uid, ServerSession serverSession) {
        serverSessionMap.put(uid, serverSession);
        log.info("当前所有用户 {}", serverSessionMap.keySet());
    }

    public static void removeSession(String uid) {
        serverSessionMap.remove(uid);
    }

}
