package org.zk.imServer.process;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zk.common.beans.User;
import org.zk.common.beans.msg.ProtoMsg;
import org.zk.common.concurrent.CallbackTask;
import org.zk.imServer.protoConverter.LoginResponseConvert;
import org.zk.imServer.session.ServerSession;
import org.zk.imServer.session.SessionMap;

/**
 * @author zhangkang
 * @date 2024/12/15 20:38
 */
@Component
@Slf4j
public class LoginProcess {

    public Boolean process(ServerSession session, ProtoMsg.Message message) {
        // 业务线程池中执行
        ProtoMsg.LoginRequest loginRequest = message.getLoginRequest();
        User user = new User();
        user.setUid(loginRequest.getUid());
        user.setPassword(loginRequest.getPassword());
        session.setUser(user);
        // 调用接口，验证用户名密码
        log.info("登录成功 {}", user);

        SessionMap.putServerSession(user.getUid(), session);

        ProtoMsg.Message loginResp = LoginResponseConvert.build(message.getSequence(), true, "登录成功");
        session.getChannel().writeAndFlush(loginResp);
        return true;
    }
}
