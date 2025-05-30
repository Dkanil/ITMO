package com.lab7.server.commands;

import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.common.validators.EmptyValidator;
import com.lab7.server.managers.DBManager;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;

public class ShowUserList extends Command<EmptyValidator> {
    public ShowUserList() {
        super(CommandNames.SHOW_USER_LIST.getName(), CommandNames.SHOW_USER_LIST.getDescription(), new EmptyValidator());
    }

    @Override
    protected ExecutionStatus runInternal(String arg, Pair<String, String> user) {
        return DBManager.getInstance().showUserList(user);
    }
}
