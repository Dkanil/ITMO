package com.lab7.server.managers;

import com.lab7.common.models.MusicBand;
import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.server.Server;

public class AuthenticatedExecutor implements ExecutorInterface {
    private final Executor executor;

    public AuthenticatedExecutor(Executor executor) {
        this.executor = executor;
    }

    @Override
    public ExecutionStatus runCommand(String[] command, MusicBand band, Pair<String, String> user) {
        ExecutionStatus authStatus;
        if (command[0].equals("register") || command[0].equals("login")) {
            authStatus = "register".equals(command[0])
                    ? DBManager.getInstance().addUser(user)
                    : DBManager.getInstance().checkPassword(user);
            if (authStatus.isSuccess()) {
                Server.logger.info(authStatus.getMessage() + " User: " + user.getFirst());
            }
        } else {
            authStatus = DBManager.getInstance().checkPassword(user);
            if (authStatus.isSuccess()) {
                ExecutionStatus commandStatus = executor.runCommand(command, band, user);
                if (commandStatus.isSuccess()) {
                    Server.logger.info("Command '" + command[0] + "' executed successfully for user: " + user.getFirst());
                }
                return commandStatus;
            }
        }
        return authStatus;
    }
}
