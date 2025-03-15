package org.zk.imClient.command;

import java.util.Scanner;

public interface ICommand {

    String getKey();

    void execute(Scanner scanner);
}
