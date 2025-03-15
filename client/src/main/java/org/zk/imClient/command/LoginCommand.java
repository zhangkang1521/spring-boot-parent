package org.zk.imClient.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.zk.common.beans.User;

import java.util.Scanner;

/**
 * @author zhangkang
 * @date 2024/12/14 19:46
 */
@Component
@Slf4j
public class LoginCommand implements ICommand {

    private User user = new User();

    @Override
    public String getKey() {
        return "1";
    }

    @Override
    public void execute(Scanner scanner) {
        log.info("请输入用户名@密码");
        String line = scanner.nextLine();
        String[] split = line.split("@");
        user.setUid(split[0]);
        user.setPassword(split[1]);
    }

    public User getUser() {
        return user;
    }
}
