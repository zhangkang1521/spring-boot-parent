package org.zk.imClient.command;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Scanner;

/**
 * @author zhangkang
 * @date 2024/12/14 19:50
 */
@Component
@Slf4j
public class ChatCommand implements ICommand{

    private String toUid;

    private String content;

    @Override
    public String getKey() {
        return "2";
    }

    @Override
    public void execute(Scanner scanner) {
        log.info("请输入toUid:content");
        String line = scanner.nextLine();
        String[] split = line.split(":");
        toUid = split[0];
        content = split[1];
    }

    public String getToUid() {
        return toUid;
    }

    public void setToUid(String toUid) {
        this.toUid = toUid;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
