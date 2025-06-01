package com.lab8.server.commands;

import com.lab8.common.utility.ExecutionStatus;
import com.lab8.common.utility.Pair;
import com.lab8.common.validators.EmptyValidator;
import com.lab8.server.managers.DBManager;
import com.lab8.server.utility.Command;
import com.lab8.server.utility.CommandNames;

public class ShowUserList extends Command<EmptyValidator> {
    public ShowUserList() {
        super(CommandNames.SHOW_USER_LIST.getName(), CommandNames.SHOW_USER_LIST.getDescription(), new EmptyValidator());
    }

    @Override
    protected ExecutionStatus runInternal(String arg, Pair<String, String> user) {
        return DBManager.getInstance().showUserList(user);
    }
}
