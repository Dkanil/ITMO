package com.lab8.server.commands;

import com.lab8.common.utility.ExecutionStatus;
import com.lab8.common.utility.Pair;
import com.lab8.common.validators.UserPermissionValidator;
import com.lab8.server.managers.DBManager;
import com.lab8.server.utility.Command;
import com.lab8.server.utility.CommandNames;
import com.lab8.common.utility.PermissionType;

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
