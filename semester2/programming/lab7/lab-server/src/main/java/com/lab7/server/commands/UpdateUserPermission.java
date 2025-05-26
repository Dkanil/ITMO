package com.lab7.server.commands;

import com.lab7.common.utility.ExecutionStatus;
import com.lab7.common.utility.Pair;
import com.lab7.common.validators.UserPermissionValidator;
import com.lab7.server.managers.DBManager;
import com.lab7.server.utility.Command;
import com.lab7.server.utility.CommandNames;
import com.lab7.common.utility.PermissionType;

public class UpdateUserPermission extends Command<UserPermissionValidator> {
    public UpdateUserPermission() {
        super(CommandNames.UPDATE_USER_PERMISSION.getName(), CommandNames.UPDATE_USER_PERMISSION.getDescription(), new UserPermissionValidator());
    }

    @Override
    public ExecutionStatus runInternal(String argument, Pair<String, String> user) {
        String[] args = argument.split(" ");
        String username = args[0];
        PermissionType newPermission = PermissionType.valueOf(args[1]);
        return DBManager.getInstance().updateUserPermissions(username, newPermission);
    }
}
